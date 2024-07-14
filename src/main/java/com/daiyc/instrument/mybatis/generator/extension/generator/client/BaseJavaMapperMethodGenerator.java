package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * @author daiyc
 */
public abstract class BaseJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {

    protected CustomTableMyBatis3Impl getCustomTable() {
        return (CustomTableMyBatis3Impl) introspectedTable;
    }

    protected boolean enableSoftDelete() {
        return getCustomTable().enableSoftDelete();
    }
}
