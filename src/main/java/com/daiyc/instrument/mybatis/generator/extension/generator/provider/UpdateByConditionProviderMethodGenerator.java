package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes;
import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.*;

import java.util.Set;

/**
 * @author daiyc
 */
public class UpdateByConditionProviderMethodGenerator extends BaseJavaProviderMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType fqjt = introspectedTable.getRules().calculateAllFieldsClass();
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes(fqjt);

        Method method = new Method("updateByCondition");
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(fqjt, "row"));
        method.addParameter(new Parameter(CriteriaLibTypes.CONDITION, "condition"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper(), "limit"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        MethodGenerateHelper.buildUpdateSelectiveSetClause(method, introspectedTable, "row.");

        method.addBodyLine("sql.WHERE(buildCondition(condition));");

        if (enableSoftDelete()) {
            method.addBodyLine("sql.WHERE(\"" + MethodGenerateHelper.buildNotDeletedCondition(introspectedTable) + "\");");
        }

        method.addBodyLine("if (limit != null) {");
        method.addBodyLine("sql.LIMIT(limit);");
        method.addBodyLine("}");

        method.addBodyLine("");

        method.addBodyLine("return sql.toString();");

        topLevelClass.addImportedTypes(importedTypes);
        topLevelClass.addMethod(method);
    }
}
