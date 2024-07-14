package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * @author daiyc
 */
public class InsertBulkSelectiveProviderMethodGenerator extends BaseJavaProviderMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Method method = new Method("insertBulk");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());

        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes(fqjt);

        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());
        FullyQualifiedJavaType parameterType = FullyQualifiedJavaType.getNewListInstance();
        parameterType.addTypeArgument(introspectedTable.getRules().calculateAllFieldsClass());
        method.addParameter(new Parameter(parameterType, "rows"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        method.addBodyLine("SQL sql = new SQL();");

        method.addBodyLine(String.format("sql.INSERT_INTO(\"%s\");",
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));

        List<IntrospectedColumn> insertColumns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());

        String columnNamesStr = insertColumns
                .stream()
                .map(column -> "\"" + escapeStringForJava(getEscapedColumnName(column)) + "\"")
                .collect(Collectors.joining(", "));
        method.addBodyLine("sql.INTO_COLUMNS(" + columnNamesStr + ");");

        method.addBodyLine("for (int i = 0; i < rows.size(); i++) {");
        method.addBodyLine(fqjt.getShortName() + " row = rows.get(i);");
        for (IntrospectedColumn introspectedColumn : insertColumns) {
            method.addBodyLine(String.format("if (row.%s() != null) {", getGetterMethodName(introspectedColumn.getJavaProperty(),
                    introspectedColumn.getFullyQualifiedJavaType())));

            method.addBodyLine(String.format("sql.INTO_VALUES(\"%s\");", getParameterClause(introspectedColumn, "rows[\" + i + \"].")));

            method.addBodyLine("} else {");
            method.addBodyLine(String.format("sql.INTO_VALUES(\"%s\");", introspectedColumn.getDefaultValue()));
            method.addBodyLine("}");
        }
        method.addBodyLine("sql.ADD_ROW();");
        method.addBodyLine("}");

        method.addBodyLine("");
        method.addBodyLine("return sql.toString();");

        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
    }
}
