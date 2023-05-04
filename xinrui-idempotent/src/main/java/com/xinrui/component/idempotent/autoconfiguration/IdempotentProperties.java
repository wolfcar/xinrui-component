package com.xinrui.component.idempotent.autoconfiguration;

import com.xinrui.component.idempotent.storage.IdempotentStorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author jerry
 */
@Data
@ConfigurationProperties(prefix = "com.xinrui.component")
public class IdempotentProperties {

    /**
     * 一级存储类型
     * @see IdempotentStorageTypeEnum
     */
    private String firstLevelType = IdempotentStorageTypeEnum.REDIS.name();

    /**
     * 二级存储类型
     * @see IdempotentStorageTypeEnum
     */
    private String secondLevelType;

}
