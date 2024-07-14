package com.daiyc.instrument.mybatis.generator.extension.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author daiyc
 */
@RequiredArgsConstructor
@Getter
public enum TableProperty {
    /**
     * 是否生成 schema 类
     */
    GENERATE_SCHEMA("generateSchema"),

    /**
     * 软删列名
     */
    SOFT_DELETE_COLUMN("softDeleteColumn"),

    /**
     * 未删除的值
     */
    NOT_DELETED_VALUE("notDeletedValue"),

    /**
     * 已删除的值
     */
    NEW_DELETED_VALUE("newDeletedValue"),
    ;

    private final String name;
}
