package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.*;

import static com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper.getColumnGetterMethodName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * @author daiyc
 */
public class UpsertProviderMethodGenerator extends BaseJavaProviderMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();
        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.ArrayList"));

        Method method = new Method("upsert");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, "row"));

        method.addBodyLine("String sql = " + introspectedTable.getInsertSelectiveStatementId() + "(row);");
        method.addBodyLine("List<String> sb = new ArrayList<>();");

        MethodGenerateHelper.forEachUpdatableField(introspectedTable, (column, isPrimitive, isFirst) -> {
            String columnName = escapeStringForJava(getEscapedColumnName(column));
            String str = String.format("sb.add(\"%s = VALUES(%s)\");", columnName, columnName);
            if (!isPrimitive) {
                method.addBodyLine(String.format("if (row.%s() != null) {", getColumnGetterMethodName(column)));
                method.addBodyLine(str);
                method.addBodyLine("}");
            } else {
                method.addBodyLine(str);
            }
            return null;
        });
        method.addBodyLine("return sql + \" ON DUPLICATE KEY UPDATE \" + String.join(\",\\n\", sb);");
        topLevelClass.addMethod(method);
    }
}
