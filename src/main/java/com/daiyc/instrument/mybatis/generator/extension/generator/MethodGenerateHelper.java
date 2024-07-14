package com.daiyc.instrument.mybatis.generator.extension.generator;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomColumnImpl;
import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import com.daiyc.instrument.mybatis.generator.extension.enums.TableProperty;
import io.vavr.Function3;
import io.vavr.collection.Stream;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * @author daiyc
 */
public abstract class MethodGenerateHelper {
    public static List<String> buildByPrimaryKeyWhereClause(IntrospectedTable introspectedTable) {
        List<String> answer = new ArrayList<>();
        String str = Stream.ofAll(introspectedTable.getPrimaryKeyColumns())
                .map(introspectedColumn -> String.format("%s = %s",
                        escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                        getParameterClause(introspectedColumn)))
                .append(buildNotDeletedCondition(introspectedTable))
                .filter(StringUtils::isNotBlank)
                .intersperse(" and ")
                .fold("", String::concat);

        answer.add(String.format("    \"where %s\"", str));
        return answer;
    }

    public static String buildNotDeletedCondition(IntrospectedTable introspectedTable) {
        CustomTableMyBatis3Impl table = (CustomTableMyBatis3Impl) introspectedTable;
        if (!table.enableSoftDelete()) {
            return "";
        }

        CustomColumnImpl softDeleteColumn = table.getSoftDeleteColumn();
        String softDeleteValue = table.getStringProperty(TableProperty.NOT_DELETED_VALUE);

        return String.format("%s = %s", getEscapedColumnName(softDeleteColumn), softDeleteValue);
    }
    public static void buildUpdateSelectiveSetClause(Method method, IntrospectedTable introspectedTable) {
        buildUpdateSelectiveSetClause(method, introspectedTable, null);
    }

    public static void buildUpdateSelectiveSetClause(Method method, IntrospectedTable introspectedTable, String prefix) {
        method.addBodyLine("SQL sql = new SQL();");

        method.addBodyLine(String.format("sql.UPDATE(\"%s\");",
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("");

        for (IntrospectedColumn introspectedColumn :
                ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns())) {
            if (!isUpdatable(introspectedTable, introspectedColumn)) {
                continue;
            }
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine(String.format("if (row.%s() != null) {",
                        getColumnGetterMethodName(introspectedColumn)));
            }

            method.addBodyLine(String.format("sql.SET(\"%s = %s\");",
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn, prefix)));

            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}");
            }

            method.addBodyLine("");
        }
    }

    public static void forEachUpdatableField(IntrospectedTable introspectedTable, Function3<IntrospectedColumn, Boolean, Boolean, Void> columnConsumer) {
        boolean isFirst = true;
        for (IntrospectedColumn introspectedColumn :
                ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns())) {
            if (!isUpdatable(introspectedTable, introspectedColumn)) {
                continue;
            }
            columnConsumer.apply(introspectedColumn, introspectedColumn.getFullyQualifiedJavaType().isPrimitive(), isFirst);
            isFirst = false;
        }
    }

    public static String getColumnGetterMethodName(IntrospectedColumn introspectedColumn) {
        return JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(),
                introspectedColumn.getFullyQualifiedJavaType());
    }

    protected static boolean isUpdatable(IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        CustomTableMyBatis3Impl table = (CustomTableMyBatis3Impl) introspectedTable;
        return !table.enableSoftDelete() || !table.getSoftDeleteColumn().equals(introspectedColumn);
    }

    public static Method overloadMethod(Method originalMethod, List<String> overParams) {
        Method method = new Method(originalMethod);
        method.setAbstract(false);
        method.setDefault(true);
        method.getParameters().clear();

        List<Parameter> parameters = originalMethod.getParameters();

        StringBuilder psb = new StringBuilder();

        for (int i = 0; i < overParams.size(); i++) {
            String p = overParams.get(i);
            Parameter parameter = parameters.get(i);
            if (i != 0) {
                psb.append(", ");
            }
            if (p == null) {
                method.addParameter(new Parameter(parameter.getType(), parameter.getName()));
                psb.append(parameter.getName());
            } else {
                psb.append(p);
            }
        }

        method.addBodyLine("return this." + originalMethod.getName() + "(" + psb + ");");
        return method;
    }
}
