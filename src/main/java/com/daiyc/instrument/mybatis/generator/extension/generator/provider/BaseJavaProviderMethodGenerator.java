package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider.AbstractJavaProviderMethodGenerator;

/**
 * @author daiyc
 */
public abstract class BaseJavaProviderMethodGenerator extends AbstractJavaProviderMethodGenerator {
    protected CustomTableMyBatis3Impl getCustomTable() {
        return (CustomTableMyBatis3Impl) introspectedTable;
    }

    protected boolean enableSoftDelete() {
        return getCustomTable().enableSoftDelete();
    }
}
