package com.xinrui.component.idempotent.biz;

import com.alibaba.fastjson2.JSON;
import com.xinrui.component.idempotent.autoconfiguration.IdempotentProperties;
import com.xinrui.component.idempotent.dto.IdempotentParam;
import com.xinrui.component.idempotent.exception.IdempotentExecuteException;
import com.xinrui.component.idempotent.storage.IdempotentStorage;
import com.xinrui.component.idempotent.storage.IdempotentStorageFactory;
import com.xinrui.component.idempotent.storage.IdempotentStorageTypeEnum;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <b><code>IdemponentBiz</code></b>
 * <p/>
 * <b>Description</b>
 * <p>
 * <p/>
 * <b>Creation Time:</b> 2023/4/27 01:19
 *
 * @author jerry chedejun@126.com
 * @since 1.0.0
 */
public class IdempotentBiz {


    private IdempotentStorageFactory idempotentStorageFactory;

    private IdempotentProperties idempotentProperties;

    public IdempotentBiz(IdempotentStorageFactory idempotentStorageFactory, IdempotentProperties idempotentProperties) {
        this.idempotentStorageFactory = idempotentStorageFactory;
        this.idempotentProperties = idempotentProperties;
    }

    <T> T idemponent(IdempotentParam<T> idempotentParam, Supplier<T> supplier) {
        idempotentStorageFactory.getIdempotentStorage(IdempotentStorageTypeEnum.valueOf(idempotentProperties.getFirstLevelType()));
        IdempotentStorage firstIdempotentStorage = idempotentStorageFactory.getIdempotentStorage(IdempotentStorageTypeEnum.valueOf(idempotentProperties.getFirstLevelType()));
        String firstValue = firstIdempotentStorage.getValue(idempotentParam.getIdempotentKey());
        IdempotentStorage secondIdempotentStorage = null;
        if (StringUtils.hasText(idempotentProperties.getSecondLevelType())) {
            secondIdempotentStorage = idempotentStorageFactory.getIdempotentStorage(IdempotentStorageTypeEnum.valueOf(idempotentProperties.getSecondLevelType()));
        }
        String secondValue = null;
        if (secondIdempotentStorage != null) {
            secondValue = secondIdempotentStorage.getValue(idempotentParam.getIdempotentKey());
        }

        // 一级和二级存储中都没有数据，表示可以继续执行
        if (!StringUtils.hasText(firstValue) && !StringUtils.hasText(secondValue)) {
            T executeResult = supplier.get();
            String resultValue = Objects.nonNull(executeResult) ? JSON.toJSONString(executeResult) : "{}";

            firstIdempotentStorage.setValue(idempotentParam.getIdempotentKey(), resultValue, idempotentParam.getFirstLevelExpireTime(), TimeUnit.SECONDS);
            if (secondIdempotentStorage != null) {
                secondIdempotentStorage.setValue(idempotentParam.getIdempotentKey(), resultValue, idempotentParam.getSecondLevelExpireTime(), TimeUnit.SECONDS);
            }
            return executeResult;
        }
        if (!idempotentParam.isRepeat()) {
            throw new IdempotentExecuteException("触发幂等，重复的交易");
        }

        String cacheResultString = StringUtils.hasText(firstValue) ? firstValue : secondValue;
        return JSON.parseObject(cacheResultString, idempotentParam.getResultType());

    }
}
