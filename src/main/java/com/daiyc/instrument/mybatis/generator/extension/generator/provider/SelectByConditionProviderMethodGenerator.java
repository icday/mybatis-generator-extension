package com.daiyc.instrument.mybatis.generator.extension.generator.provider;

import com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes;
import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.*;

import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 *         SQL sql = new SQL();
 *         sql.SELECT("*");
 *         sql.FROM("movie");
 *         sql.WHERE(buildCondition(condition));
 *         sql.WHERE("deleted_at = '1970-01-01 08:00:00'");
 *
 *         if (StringUtils.isNotBlank(orderBy)) {
 *             sql.ORDER_BY(orderBy);
 *         }
 *
 *         if (pagination != null) {
 *             sql.LIMIT("#{pagination.limit}");
 *             sql.OFFSET("#{pagination.offset}");
 *         }
 *
 *         return sql.toString();
 * @author daiyc
 */
public class SelectByConditionProviderMethodGenerator extends BaseJavaProviderMethodGenerator {
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Method method = new Method("selectByCondition");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(CriteriaLibTypes.CONDITION, "condition"));
        method.addParameter(new Parameter(CriteriaLibTypes.PAGINATION, "pagination"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "orderBy"));

        topLevelClass.addImportedType(CriteriaLibTypes.CONDITION);
        topLevelClass.addImportedType(CriteriaLibTypes.PAGINATION);

        method.addBodyLine("SQL sql = new SQL();");
        method.addBodyLine("sql.SELECT(\"*\");");
        method.addBodyLine("sql.FROM(\"" + escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()) + "\");");
        method.addBodyLine("sql.WHERE(buildCondition(condition));");

        if (enableSoftDelete()) {
            method.addBodyLine("sql.WHERE(\"" + MethodGenerateHelper.buildNotDeletedCondition(introspectedTable) + "\");");
        }

        method.addBodyLine("");

        method.addBodyLine("if (orderBy != null && !orderBy.isEmpty()) {");
        method.addBodyLine("sql.ORDER_BY(orderBy);");
        method.addBodyLine("}");
        method.addBodyLine("");

        method.addBodyLine("if (pagination != null) {");
        method.addBodyLine("sql.LIMIT(\"#{pagination.limit}\");");
        method.addBodyLine("sql.OFFSET(\"#{pagination.offset}\");");
        method.addBodyLine("}");

        method.addBodyLine("");

        method.addBodyLine("return sql.toString();");

        topLevelClass.addMethod(method);
    }
}
