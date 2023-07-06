package com.vegetarianlefty.common.exception;

import com.vegetarianlefty.common.response.ServiceStatus;

import javax.annotation.Nonnull;

/**
 * <p>系统异常信息</p>
 *
 * @date 2023/2/2 18:13
 */
public final class SystemIllegalException extends AbstractCustomizedRunTimeException {
    private static final long serialVersionUID = 1L;

    public SystemIllegalException(@Nonnull ServiceStatus serviceStatus) {
        super(serviceStatus);
    }

    public SystemIllegalException(@Nonnull ServiceStatus serviceStatus, @Nonnull final String message) {
        super(serviceStatus, message);
    }
}
