package com.xinrui.component.idempotent.storage;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author jerry
 */
public class IdempotentStorageFactory {

    @Autowired
    private List<IdempotentStorage> idempotentStorageList;

    public IdempotentStorage getIdempotentStorage(IdempotentStorageTypeEnum type) {
        Optional<IdempotentStorage> idempotentStorageOptional = idempotentStorageList.stream().filter(t -> t.type() == type).findAny();
        return idempotentStorageOptional.orElseThrow(NullPointerException::new);
    }

}
