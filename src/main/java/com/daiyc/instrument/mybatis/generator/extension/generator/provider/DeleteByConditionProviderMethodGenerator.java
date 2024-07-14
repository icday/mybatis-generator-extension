package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomColumnImpl;
import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import com.daiyc.instrument.mybatis.generator.extension.enums.TableProperty;
import com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes;
import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.*;

import java.util.Set;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * @author daiyc
 */
public class DeleteByConditionProviderMethodGenerator extends BaseJavaProviderMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes(fqjt);

        Method method = new Method("deleteByCondition");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(CriteriaLibTypes.CONDITION, "condition"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper(), "limit"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        if (enableSoftDelete()) {
            buildUpdateMethod(method);
        } else {
            buildDeleteMethod(method);
        }

        method.addBodyLine("sql.WHERE(buildCondition(condition));");
        if (enableSoftDelete()) {
            method.addBodyLine("sql.WHERE(\"" + MethodGenerateHelper.buildNotDeletedCondition(introspectedTable) + "\");");
        }

        method.addBodyLine("");

        method.addBodyLine("if (limit != null) {");
        method.addBodyLine("sql.LIMIT(limit);");
        method.addBodyLine("}");

        method.addBodyLine("");

        method.addBodyLine("return sql.toString();");

        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
    }

    protected void buildDeleteMethod(Method method) {
        method.addBodyLine("SQL sql = new SQL();");

        method.addBodyLine(String.format("sql.DELETE_FROM(\"%s\");",
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("");
    }

    protected void buildUpdateMethod(Method method) {
        CustomTableMyBatis3Impl table = getCustomTable();
        CustomColumnImpl softDeleteColumn = table.getSoftDeleteColumn();
        String newDeletedValue = table.getStringProperty(TableProperty.NEW_DELETED_VALUE);

        method.addBodyLine("SQL sql = new SQL();");

        method.addBodyLine(String.format("sql.UPDATE(\"%s\");",
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("");

        method.addBodyLine(String.format("sql.SET(\"%s = %s\");",
                escapeStringForJava(getEscapedColumnName(softDeleteColumn)),
                escapeStringForJava(newDeletedValue)
        ));
    }
}
