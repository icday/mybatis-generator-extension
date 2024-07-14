package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator;

import java.util.List;

/**
 * @author daiyc
 */
public class UpdateByIdWithBLOBsJavaMapperMethodGenerator extends AnnotatedUpdateByPrimaryKeyWithBLOBsMethodGenerator {
    @Override
    protected List<String> buildByPrimaryKeyWhereClause() {
        return MethodGenerateHelper.buildByPrimaryKeyWhereClause(introspectedTable);
    }

    protected CustomTableMyBatis3Impl getCustomTable() {
        return (CustomTableMyBatis3Impl) introspectedTable;
    }
}
