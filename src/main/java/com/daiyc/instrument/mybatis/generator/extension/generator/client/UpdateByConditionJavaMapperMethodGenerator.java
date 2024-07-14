package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes;
import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author daiyc
 */
public class UpdateByConditionJavaMapperMethodGenerator extends BaseJavaMapperMethodGenerator {
    protected static final String UPDATE_BY_CONDITION = "updateByCondition";

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();

        importedTypes.add(fqjt);

        Method method = new Method(UPDATE_BY_CONDITION);
        method.addParameter(new Parameter(fqjt, "row", "@Param(\"row\")"));
        method.addParameter(new Parameter(CriteriaLibTypes.CONDITION, "condition", "@Param(\"condition\")"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper(), "limit", "@Param(\"limit\")"));

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setAbstract(true);

        addExtraImports(interfaze);
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);

        Method overloadMethod = MethodGenerateHelper.overloadMethod(method, Arrays.asList(null, null, "null"));
        interfaze.addMethod(overloadMethod);

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        addMapperAnnotations(method);
    }

    public void addMapperAnnotations(Method method) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());

        String s = "@UpdateProvider(type="
                + fqjt.getShortName()
                + ".class, method=\""
                + UPDATE_BY_CONDITION
                + "\")";
        method.addAnnotation(s);
    }

    public void addExtraImports(Interface interfaze) {
        // extension point for subclasses
    }
}
