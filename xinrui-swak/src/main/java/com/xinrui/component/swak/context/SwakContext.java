package com.xinrui.component.swak.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.xinrui.component.swak.config.SwakConstants;

/**
 * <b><code>SwakContext</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/25 00:29.
 *
 * @author jerry
 * @since guoke-component
 */

public class SwakContext {

    /**
     * 线程变量，业务路由栈
     */
    private final static TransmittableThreadLocal<SwakThreadContext> THREAD_CONTEXT = TransmittableThreadLocal.withInitial(
            () -> new SwakThreadContext(SwakConstants.SWAK_DEFAULT_BIZ, SwakConstants.SWAK_DEFAULT_TAG)
    );

    /**
     * @param context a
     */
    public static void set(SwakThreadContext context) {
        if (context == null) {
            return;
        }
        THREAD_CONTEXT.set(context);
    }

    /**
     * @return a
     */
    public static SwakThreadContext get() {
        return THREAD_CONTEXT.get();
    }


    /**
     * 清理当前线程的SPI路由的prefix
     */
    public static void clear() {
        THREAD_CONTEXT.remove();
    }

}
