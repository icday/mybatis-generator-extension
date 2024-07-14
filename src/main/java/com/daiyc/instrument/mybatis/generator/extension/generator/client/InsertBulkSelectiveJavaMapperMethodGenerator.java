package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedInsertSelectiveMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author daiyc
 */
public class InsertBulkSelectiveJavaMapperMethodGenerator extends AnnotatedInsertSelectiveMethodGenerator {

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = new Method("insertBulk");

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType parameterType = FullyQualifiedJavaType.getNewListInstance();
        parameterType.addTypeArgument(introspectedTable.getRules().calculateAllFieldsClass());

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "rows"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(method);

        if (context.getPlugins().clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    @Override
    public void addMapperAnnotations(Method method) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());

        String s = "@InsertProvider(type="
                + fqjt.getShortName()
                + ".class, method=\""
                + "insertBulk"
                + "\")";
        method.addAnnotation(s);

        buildGeneratedKeyAnnotation().ifPresent(method::addAnnotation);
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        interfaze.addImportedTypes(buildGeneratedKeyImportsIfRequired());
        interfaze.addImportedType(
                new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider"));
    }
}
