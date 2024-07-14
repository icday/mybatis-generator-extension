package com.daiyc.instrument.mybatis.generator.extension.plugin;

import com.daiyc.instrument.mybatis.generator.extension.plugin.base.BasePlugin;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 使用Lombok，简化代码
 *
 * @author daiyc
 */
public class LombokPlugin extends BasePlugin {
    private final static String ANN_DATA_TYPE = "lombok.Data";

    private final static String ANN_FLUENT_TYPE = "lombok.experimental.Accessors";

    private final static String ANN_DATA_NAME = "@Data";
    private final static String ANN_FLUENT_NAME = "@Accessors(chain = true)";

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeLombok(topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeLombok(topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeLombok(topLevelClass);
        return true;
    }

    private static void makeLombok(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(ANN_DATA_TYPE);
        topLevelClass.addImportedType(ANN_FLUENT_TYPE);
        topLevelClass.addAnnotation(ANN_DATA_NAME);
        topLevelClass.addAnnotation(ANN_FLUENT_NAME);
    }
}
