package com.daiyc.instrument.mybatis.generator.extension.plugin;

import com.daiyc.instrument.mybatis.generator.extension.plugin.base.BasePlugin;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 注释优化
 *
 * @author daiyc
 */
public class CommentPlugin extends BasePlugin {
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, Plugin.ModelClassType modelClassType) {
        field.getJavaDocLines().clear();
        String remarks = introspectedColumn.getRemarks();
        if (!StringUtility.stringHasValue(remarks)) {
            return true;
        }
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return generateModelClassComment(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return generateModelClassComment(topLevelClass, introspectedTable);
    }

    private static boolean generateModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.getJavaDocLines().clear();
        String remarks = introspectedTable.getRemarks();

        if (!StringUtility.stringHasValue(remarks)) {
            return true;
        }
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine(" */");
        return true;
    }

}
