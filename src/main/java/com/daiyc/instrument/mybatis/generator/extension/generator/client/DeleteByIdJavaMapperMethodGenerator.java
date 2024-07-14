package com.daiyc.instrument.mybatis.generator.extension.generator.client;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomColumnImpl;
import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import com.daiyc.instrument.mybatis.generator.extension.enums.TableProperty;
import com.daiyc.instrument.mybatis.generator.extension.generator.MethodGenerateHelper;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;

import java.util.List;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * @author daiyc
 */
public class DeleteByIdJavaMapperMethodGenerator extends AnnotatedDeleteByPrimaryKeyMethodGenerator {
    public DeleteByIdJavaMapperMethodGenerator(boolean isSimple) {
        super(isSimple);
    }


    @Override
    public void addExtraImports(Interface interfaze) {
        if (!getIntrospectedTable().enableSoftDelete()) {
            super.addExtraImports(interfaze);
            return;
        }

        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Update"));
    }

    @Override
    public void addMapperAnnotations(Method method) {
        if (!getIntrospectedTable().enableSoftDelete()) {
            super.addMapperAnnotations(method);
            return;
        }
        method.addAnnotation("@Update({");

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"update ");
        sb.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));

        CustomColumnImpl softDeleteColumn = getIntrospectedTable().getSoftDeleteColumn();
        String newDeletedValue = getIntrospectedTable().getStringProperty(TableProperty.NEW_DELETED_VALUE);

        sb.append(String.format(" set %s = %s", escapeStringForJava(getEscapedColumnName(softDeleteColumn)), escapeStringForJava(newDeletedValue)));
        sb.append("\",");
        method.addAnnotation(sb.toString());

        buildByPrimaryKeyWhereClause().forEach(method::addAnnotation);

        method.addAnnotation("})");
    }

    @Override
    protected List<String> buildByPrimaryKeyWhereClause() {
        return MethodGenerateHelper.buildByPrimaryKeyWhereClause(introspectedTable);
    }

    protected CustomTableMyBatis3Impl getIntrospectedTable() {
        return (CustomTableMyBatis3Impl) introspectedTable;
    }
}
