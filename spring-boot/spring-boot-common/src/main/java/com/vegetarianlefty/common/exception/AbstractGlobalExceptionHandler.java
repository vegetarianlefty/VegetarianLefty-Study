package com.vegetarianlefty.common.exception;


import com.vegetarianlefty.common.response.Res;
import com.vegetarianlefty.common.response.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>程序异常处理</p>
 *
 * @date 2023/2/2 20:31
 */
@Slf4j
@Order(-1)
public abstract class AbstractGlobalExceptionHandler {


    @ExceptionHandler(value = AbstractCustomizedRunTimeException.class)
    @ResponseBody
    public Res handleCustomRunTimeException(AbstractCustomizedRunTimeException e) {
        log.error("全局异常："+e.getMessage(), e);
        return Res.get(ServiceStatus.GENERAL.BAD_REQUEST, e.getMessage());
    }
}
