package com.daiyc.instrument.mybatis.generator.extension.generator;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes.SCHEMA_FACTORY;
import static com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes.SINGLE_VALUE_TYPE;

/**
 * @author daiyc
 */
public class SchemaClassGenerator extends AbstractJavaGenerator {
    public SchemaClassGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedJavaType schemaType = getSchemaType();
        TopLevelClass schemaClass = new TopLevelClass(schemaType);
        schemaClass.setVisibility(JavaVisibility.PUBLIC);
        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getField(introspectedColumn, context, introspectedTable);
            schemaClass.addField(field);
            schemaClass.addImportedType(field.getType());
        }

        addInitBlock(schemaClass);
        addNamingStrategy(schemaClass);

        return Collections.singletonList(schemaClass);
    }

    protected Field getField(IntrospectedColumn introspectedColumn,
                             Context context,
                             IntrospectedTable introspectedTable) {
        String name = introspectedColumn.getActualColumnName().toUpperCase();
        FullyQualifiedJavaType realType = introspectedColumn.getFullyQualifiedJavaType();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("%s<%s>", SINGLE_VALUE_TYPE.getFullyQualifiedName(), realType.getFullyQualifiedName()));
        Field field = new Field(name, type);
        field.setStatic(true);
        field.setVisibility(JavaVisibility.PUBLIC);

        addFieldComment(introspectedColumn, field);

        return field;
    }

    private static void addFieldComment(IntrospectedColumn introspectedColumn, Field field) {
        String remarks = introspectedColumn.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            field.getJavaDocLines().clear();
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + remarks);
            field.addJavaDocLine(" */");
        }
    }

    protected List<IntrospectedColumn> getColumnsInThisClass() {
        return introspectedTable.getAllColumns();
    }

    protected FullyQualifiedJavaType getSchemaType() {
        return ((CustomTableMyBatis3Impl) introspectedTable).getSchemaType();
    }

    protected void addInitBlock(TopLevelClass schemaClass) {
        InitializationBlock initializationBlock = new InitializationBlock(true);
        initializationBlock.addBodyLine(String.format("%s.init(%s.class);", SCHEMA_FACTORY.getShortName(), schemaClass.getType().getShortName()));
        schemaClass.addImportedType(SCHEMA_FACTORY);

        schemaClass.addInitializationBlock(initializationBlock);
    }

    protected void addNamingStrategy(TopLevelClass schemaClass) {
        Arrays.asList(
                "com.daiyc.criteria.core.annotations.Attribute",
                "com.daiyc.criteria.core.enums.DataType",
                "com.daiyc.criteria.core.enums.PropertyNamingStrategy",
                "com.daiyc.criteria.core.enums.SchemaAttribute"
        ).forEach(schemaClass::addImportedType);

        schemaClass.addAnnotation("@Attribute(group = \"mybatis\", name = SchemaAttribute.NAMING_STRATEGY, value = PropertyNamingStrategy.LOWER_UNDERSCORE_NAME)");
    }
}
