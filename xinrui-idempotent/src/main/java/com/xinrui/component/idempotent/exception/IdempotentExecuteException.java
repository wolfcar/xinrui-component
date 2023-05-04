package com.xinrui.component.idempotent.exception;

/**
 * <b><code>IdempotentExecuteException</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/5/4 11:19
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */
public class IdempotentExecuteException extends RuntimeException{

    public IdempotentExecuteException(String message){
        super(message);
    }
}
