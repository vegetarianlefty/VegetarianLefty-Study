package com.vegetarianlefty.service;

import com.vegetarianlefty.factory.DistributedLockFactory;
import org.dromara.hutool.core.data.id.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * description
 *
 */
@Service
public class InventoryService {

    private Lock lock = new ReentrantLock();
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private DistributedLockFactory distributedLockFactory;


    public String sale() {
        String retMessage = "";
        Lock redisLock = distributedLockFactory.getDistributedLock("redis");
        redisLock.lock();
        try {
            //1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存
            if(inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001",String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: "+inventoryNumber;
                System.out.println(retMessage);
            }else{
                retMessage = "商品卖完了，o(╥��╥)o";
            }
            System.out.println("sale success");
        } finally {
            redisLock.unlock();
        }
        return retMessage;
    }

    /**
     * <p>微服务 Redis 分布式锁</p>
     * <p>优点：设置Redis 锁 并加入过期时间，防止宕机与过期，产出死锁问题，判断加锁与删除锁是不是同一个客户端，避免误删除他人锁</p>
     * <p>缺点：最后判断 判断加锁与删除锁，缺少可重入锁+锁的自动续期  </p>
     */
    public String sale2() {
        String retMessage = "";
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID()+Thread.currentThread().getId();

        //如果获取锁失败，说明有其他线程持有锁，等待10秒
        //如果获取锁成功，并执行业务逻辑，设置 key 并设置过期时间，保证原子性，防止宕机导致锁无法释放
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue,10, TimeUnit.SECONDS);
        while (Boolean.FALSE.equals(lock)){
            //暂停10毫秒，类似CAS自旋
            try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        try{
            //1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存
            if(inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001",String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: "+inventoryNumber;
                System.out.println(retMessage);
            }else{
                retMessage = "商品卖完了，o(╥﹏╥)o";
            }
        }finally {
            //判断加锁与删除锁是不是同一个客户端，同一个才行，自己只能删除自己的锁，不误删他人的
            /*if(stringRedisTemplate.opsForValue().get(key).equalsIgnoreCase(uuidValue)){
                stringRedisTemplate.delete(key);
            }*/
            //将判断+删除自己的合并为lua脚本保证原子性
            String luaScript =
                    "if (redis.call('get',KEYS[1]) == ARGV[1]) then " +
                            "return redis.call('del',KEYS[1]) " +
                            "else " +
                            "return 0 " +
                            "end";
            stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class), Arrays.asList(key), uuidValue);

        }

        return retMessage+"\t"+"服务端口号：";
    }

    /**
     * <p>单机版手写 Redis 分布式锁</p>
     *
     * @date 2025/2/10 15:31
     */
    public String sale1() {
        String retMessage = "";
        lock.lock();
        try {
            //1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存
            if(inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001",String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品，库存剩余: "+inventoryNumber;
                System.out.println(retMessage);
            }else{
                retMessage = "商品卖完了，o(╥﹏╥)o";
            }
            System.out.println("sale success");
        } finally {
            lock.unlock();
        }
        return retMessage;
    }


}
