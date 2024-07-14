package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import org.mybatis.generator.api.dom.java.*;

/**
 * @author daiyc
 */
public class UpdateByIdDelegateJavaMapperMethodGenerator extends BaseJavaMapperMethodGenerator {
    @Override
    public void addInterfaceElements(Interface interfaze) {
        addDelegateMethod(interfaze);

        addOverloadMethod(interfaze);
    }

    protected void addOverloadMethod(Interface interfaze) {
        Method method = new Method("updateById");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(false);
        method.setDefault(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();

        method.addParameter(new Parameter(parameterType, "row"));

        method.addBodyLine("return updateById(row, true);");
        interfaze.addMethod(method);
    }

    protected void addDelegateMethod(Interface interfaze) {
        Method method = new Method("updateById");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setAbstract(false);
        method.setDefault(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();

        method.addParameter(new Parameter(parameterType, "row"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "isSelective"));

        method.addBodyLine("if (isSelective) {");
        method.addBodyLine("return " + introspectedTable.getUpdateByPrimaryKeySelectiveStatementId() + "(row);");
        method.addBodyLine("} else {");
        method.addBodyLine("return " + introspectedTable.getUpdateByPrimaryKeyWithBLOBsStatementId() + "(row);");
        method.addBodyLine("}");
        interfaze.addMethod(method);
    }
}
