package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.*;

import java.util.*;

import static com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes.CONDITION;
import static com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes.PAGINATION;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author daiyc
 */
public class SelectByConditionJavaMapperMethodGenerator extends BaseJavaMapperMethodGenerator {
    protected List<Parameter> parameters;

    protected List<List<String>> overloadDefaultValues = Arrays.asList(
            Arrays.asList(null, null, "null"),
            Arrays.asList(null, "null", null),
            Arrays.asList(null, "null", "null")
    );

    public SelectByConditionJavaMapperMethodGenerator() {
        parameters = new ArrayList<>();
        parameters.add(new Parameter(CONDITION, "condition", "@Param(\"condition\")"));
        parameters.add(new Parameter(PAGINATION, "pagination", "@Param(\"pagination\")"));
        parameters.add(new Parameter(FullyQualifiedJavaType.getStringInstance(), "orderBy", "@Param(\"orderBy\")"));
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();

        Method method = buildOverloadedMethod(importedTypes);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);

        addOverloadMethods(interfaze, method);

        addMapperAnnotations(interfaze, method);
        addAnnotatedResults(interfaze, method, introspectedTable.getNonPrimaryKeyColumns());
    }

    protected Method buildOverloadedMethod(Set<FullyQualifiedJavaType> importedTypes) {
        importedTypes.add(CONDITION);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method("selectByCondition");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(true);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        if (introspectedTable.getRules().generateBaseRecordClass()) {
            listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        } else if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            listType = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        } else {
            throw new RuntimeException(getString("RuntimeError.12"));
        }

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);

        for (Parameter parameter : parameters) {
            method.addParameter(parameter);
        }

        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        importedTypes.add(PAGINATION);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        return method;
    }

    protected void addOverloadMethods(Interface interfaze, Method originalMethod)  {
        for (List<String> overloadDefaultValue : overloadDefaultValues) {
            Method method = MethodGenerateHelper.overloadMethod(originalMethod, overloadDefaultValue);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());
        String s = String.format("@SelectProvider(type=%s.class, method=\"selectByCondition\")", fqjt.getShortName());
        method.addAnnotation(s);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider"));
    }

    private void addResultMapAnnotation(Method method) {
        String annotation = String.format("@ResultMap(\"%s.%s\")",
                introspectedTable.getMyBatis3SqlMapNamespace(),
                introspectedTable.getRules().generateResultMapWithBLOBs()
                        ? introspectedTable.getResultMapWithBLOBsId() : introspectedTable.getBaseResultMapId());
        method.addAnnotation(annotation);
    }
}
