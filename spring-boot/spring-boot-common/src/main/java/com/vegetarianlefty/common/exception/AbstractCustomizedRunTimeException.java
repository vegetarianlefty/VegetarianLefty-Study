package com.vegetarianlefty.common.exception;

import com.vegetarianlefty.common.response.ServiceStatus;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * <p>全局异常处理</p>
 *
 * @date 2023/2/2 16:42
 */
@Getter
public abstract class AbstractCustomizedRunTimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final ServiceStatus serviceStatus;

    public AbstractCustomizedRunTimeException(@Nonnull final ServiceStatus serviceStatus) {
        this(serviceStatus, serviceStatus.getMessage());
    }

    public AbstractCustomizedRunTimeException(
            @Nonnull final ServiceStatus serviceStatus, final String message) {
        super(message);
        Objects.requireNonNull(serviceStatus, "Arg 'serviceStatus' can not be null");
        this.serviceStatus = serviceStatus;
    }
}
