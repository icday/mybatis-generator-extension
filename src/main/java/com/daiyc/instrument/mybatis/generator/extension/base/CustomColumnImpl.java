package com.daiyc.instrument.mybatis.generator.extension.base;

import org.mybatis.generator.api.IntrospectedColumn;

/**
 * @author daiyc
 */
public class CustomColumnImpl extends IntrospectedColumn {
    @Override
    public CustomTableMyBatis3Impl getIntrospectedTable() {
        return (CustomTableMyBatis3Impl) super.getIntrospectedTable();
    }
}
