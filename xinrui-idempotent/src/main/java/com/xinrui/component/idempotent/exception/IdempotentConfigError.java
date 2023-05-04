package com.xinrui.component.idempotent.exception;

import lombok.Getter;

/**
 * <b><code>幂等配置异常</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/4/27 01:40
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */

public class IdempotentConfigError extends Error {

    @Getter
    private String code;

   public IdempotentConfigError(String message) {
        super(message);
    }

    public IdempotentConfigError(String code, String message) {
        super(message);
        this.code = code;

    }

    public IdempotentConfigError(String code, String message,Throwable throwable) {
        super(message,throwable);
        this.code = code;

    }
}
