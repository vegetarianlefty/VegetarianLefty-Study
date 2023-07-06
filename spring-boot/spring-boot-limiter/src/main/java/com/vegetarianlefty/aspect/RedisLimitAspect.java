package com.vegetarianlefty.aspect;

import com.google.common.collect.ImmutableList;
import com.vegetarianlefty.annotation.RedisLimit;
import com.vegetarianlefty.common.exception.SystemIllegalException;
import com.vegetarianlefty.common.response.ServiceStatus;
import com.vegetarianlefty.enums.LimitType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 限流注解切面 aop 实现
 *
 * @author wxh_work@163.com
 * @date 2023/7/6 14:24
 */
@Slf4j
@Aspect
@Configuration
public class RedisLimitAspect {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(com.vegetarianlefty.annotation.RedisLimit)")
    public Object around(ProceedingJoinPoint pjp){
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        RedisLimit annotation = method.getAnnotation(RedisLimit.class);
        LimitType limitType = annotation.limitType();

        String name = annotation.name();
        String key;

        int period = annotation.period();
        int count = annotation.count();

        switch (limitType){
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = annotation.key();
                break;
            default:
                key =method.getName();
        }
        ImmutableList<String> keys = ImmutableList.of(annotation.prefix(), key);
        try {
            String luaScript = buildLuaScript();
            DefaultRedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number number = this.redisTemplate.execute(redisScript, keys, count, period);
            log.info("Access try count is {} for name = {} and key = {}", number, name, key);
            if(number != null && number.intValue() == 1){
                return pjp.proceed();
            }
            throw new SystemIllegalException(ServiceStatus.GENERAL.SERVICE_BUSY, annotation.msg());
        }catch (Throwable e){
            if(e instanceof SystemIllegalException){
                log.debug("令牌桶={}，获取令牌失败",key);
                throw new SystemIllegalException(ServiceStatus.GENERAL.SERVICE_BUSY, annotation.msg());
            }
            e.printStackTrace();
            throw new RuntimeException("服务器异常");
        }
    }

    public String buildLuaScript(){
        return "redis.replicate_commands(); local listLen,time" +
                "\nlistLen = redis.call('LLEN', KEYS[1])" +
                // 不超过最大值，则直接写入时间
                "\nif listLen and tonumber(listLen) < tonumber(ARGV[1]) then" +
                "\nlocal a = redis.call('TIME');" +
                "\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])" +
                "\nelse" +
                // 取出现存的最早的那个时间，和当前时间比较，看是小于时间间隔
                "\ntime = redis.call('LINDEX', KEYS[1], -1)" +
                "\nlocal a = redis.call('TIME');" +
                "\nif a[1]*1000000+a[2] - time < tonumber(ARGV[2])*1000000 then" +
                // 访问频率超过了限制，返回0表示失败
                "\nreturn 0;" +
                "\nelse" +
                "\nredis.call('LPUSH', KEYS[1], a[1]*1000000+a[2])" +
                "\nredis.call('LTRIM', KEYS[1], 0, tonumber(ARGV[1])-1)" +
                "\nend" +
                "\nend" +
                "\nreturn 1;";
    }

    public String getIpAddress(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
