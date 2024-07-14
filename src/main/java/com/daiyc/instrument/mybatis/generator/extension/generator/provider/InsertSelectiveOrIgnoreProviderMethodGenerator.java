package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider.AbstractJavaProviderMethodGenerator;

/**
 * @author daiyc
 */
public class InsertSelectiveOrIgnoreProviderMethodGenerator extends AbstractJavaProviderMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();

        Method method = new Method("insertOrIgnore");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "row"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        method.addBodyLine("String sql = " + introspectedTable.getInsertSelectiveStatementId() + "(row);");
        method.addBodyLine("return sql.replaceFirst(\"INSERT INTO\", \"INSERT IGNORE INTO\");");
        topLevelClass.addMethod(method);
    }
}
