package com.xinrui.component.idempotent.storage;

import lombok.Data;

import java.util.Date;

/**
 *
 */
@Data
public class IdempotentRecord {



    private String id;

    private String key;

    private String value;

    private Date addTime;

    private Date expireTime;

}
