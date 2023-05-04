package com.xinrui.component.idempotent.exception;

/**
 * <b><code>DistributeLockFailException</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/4/27 02:36
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */
public class DistributeLockFailException extends RuntimeException{

    public DistributeLockFailException(String message){
        super(message);
    }
}
