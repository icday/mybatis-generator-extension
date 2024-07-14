package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;

import java.util.List;

/**
 * @author daiyc
 */
public class SelectByIdJavaMapperMethodGenerator extends AnnotatedSelectByPrimaryKeyMethodGenerator {
    public SelectByIdJavaMapperMethodGenerator() {
        super(false, false);
    }

    @Override
    protected List<String> buildByPrimaryKeyWhereClause() {
        return MethodGenerateHelper.buildByPrimaryKeyWhereClause(introspectedTable);
    }
}
