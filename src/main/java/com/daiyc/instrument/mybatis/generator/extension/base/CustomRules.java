package com.daiyc.instrument.mybatis.generator.extension.base;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.rules.FlatModelRules;

/**
 * @author daiyc
 */
public class CustomRules extends FlatModelRules {
    public CustomRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }

    @Override
    public boolean generateDeleteByExample() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateSQLExampleWhereClause() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateMyBatis3UpdateByExampleWhereClause() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateSelectByExampleWithoutBLOBs() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateSelectByExampleWithBLOBs() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateExampleClass() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateCountByExample() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateUpdateByExampleSelective() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateUpdateByExampleWithoutBLOBs() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateUpdateByExampleWithBLOBs() {
        return needGenerateExampleClass();
    }

    @Override
    public boolean generateUpdateByPrimaryKeyWithoutBLOBs() {
        return false;
    }

    protected boolean needGenerateExampleClass() {
        return false;
    }
}
