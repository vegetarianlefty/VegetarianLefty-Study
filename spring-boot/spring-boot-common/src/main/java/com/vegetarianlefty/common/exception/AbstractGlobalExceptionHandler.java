package com.vegetarianlefty.common.exception;


import com.vegetarianlefty.common.response.Res;
import com.vegetarianlefty.common.response.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
            log.error("全局请求参数异常："+e.getMessage(), e);
            BindingResult bindingResult = e.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder sb = new StringBuilder();
            for (ObjectError error : allErrors) {
                FieldError fieldError = (FieldError) error;
                sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(";");
            }
            return Res.get(ServiceStatus.GENERAL.BAD_REQUEST, sb.toString());
        }
}
