package com.xinrui.component.swak.context;

import com.xinrui.component.swak.config.SwakConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <b><code>SwakContext</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2022/10/25 00:20.
 *
 * @author jerry
 * @since guoke-component
 */
@Data
@AllArgsConstructor
public class SwakThreadContext {

    private String bizCode;

    private String tag;


    public static SwakThreadContext defaultContext() {
        return new SwakThreadContext(SwakConstants.SWAK_DEFAULT_BIZ, SwakConstants.SWAK_DEFAULT_TAG);
    }

}
