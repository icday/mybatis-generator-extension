package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider.ProviderUpdateByPrimaryKeySelectiveMethodGenerator;

import java.util.Set;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * @author daiyc
 */
public class UpdateByIdSelectiveProviderMethodGenerator extends ProviderUpdateByPrimaryKeySelectiveMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes(fqjt);

        Method method = new Method(introspectedTable.getUpdateByPrimaryKeySelectiveStatementId());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, "row"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        MethodGenerateHelper.buildUpdateSelectiveSetClause(method, introspectedTable);

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            String sb = String.format("sql.WHERE(\"%s = %s",
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn)) +
                    "\");";
            method.addBodyLine(sb);
        }
        String s = MethodGenerateHelper.buildNotDeletedCondition(introspectedTable);

        if (StringUtils.isNotBlank(s)) {
            method.addBodyLine("sql.WHERE(\"" + s + "\");");
        }

        method.addBodyLine("");

        method.addBodyLine("return sql.toString();");

        if (context.getPlugins()
                .providerUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}
