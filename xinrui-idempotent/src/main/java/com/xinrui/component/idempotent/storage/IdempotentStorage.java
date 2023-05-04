package com.xinrui.component.idempotent.storage;

import java.util.concurrent.TimeUnit;

/**
 * 幂等存储接口
 *
 * @author jerry
 */
public interface IdempotentStorage {

    String COLL_NAME = "idempotent_record";

    IdempotentStorageTypeEnum type();

    void setValue(String key, String value, long expireTime, TimeUnit timeUnit);

    String getValue(String key);

}
