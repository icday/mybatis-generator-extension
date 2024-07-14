package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author daiyc
 */
public class UpsertJavaMapperMethodGenerator extends AbstractJavaMapperMethodGenerator {
    private static final String METHOD_NAME = "upsert";

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = new Method(METHOD_NAME);

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "row"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(method);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    public void addMapperAnnotations(Method method) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());

        String s = "@InsertProvider(type="
                + fqjt.getShortName()
                + ".class, method=\""
                + METHOD_NAME
                + "\")";
        method.addAnnotation(s);

        buildGeneratedKeyAnnotation().ifPresent(method::addAnnotation);
    }
}
