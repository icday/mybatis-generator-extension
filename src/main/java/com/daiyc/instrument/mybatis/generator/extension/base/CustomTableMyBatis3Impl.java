package com.daiyc.instrument.mybatis.generator.extension.base;

import com.daiyc.instrument.mybatis.generator.extension.enums.TableProperty;
import com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaJavaClientGenerator;
import com.daiyc.instrument.mybatis.generator.extension.generator.SchemaClassGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * flat
 *
 * @author daiyc
 */
public class CustomTableMyBatis3Impl extends IntrospectedTableMyBatis3Impl {
    @Override
    public void initialize() {
        super.initialize();
        this.rules = new CustomRules(this);
        this.setInsertSelectiveStatementId("insert");

        this.setUpdateByExampleSelectiveStatementId("updateByCondition");
//        this.setUpdateByPrimaryKeySelectiveStatementId("updateById");
        this.setUpdateByPrimaryKeyWithBLOBsStatementId("updateByPrimaryKey");
        this.setSelectByPrimaryKeyStatementId("getById");
//        this.setSelectByExampleStatementId("selectByCondition");
        this.setSelectByExampleWithBLOBsStatementId("selectByCondition");
        this.setDeleteByPrimaryKeyStatementId("deleteById");
        this.setDeleteByExampleStatementId("deleteByCondition");
        this.setCountByExampleStatementId("countByCondition");
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        super.calculateJavaModelGenerators(warnings, progressCallback);

        if (shouldGenerateSchema()) {
            AbstractJavaGenerator schemaGenerator = new SchemaClassGenerator(getExampleProject());
            initializeAbstractGenerator(schemaGenerator, warnings, progressCallback);
            javaGenerators.add(schemaGenerator);
        }
    }

    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        AbstractJavaClientGenerator javaClientGenerator = super.createJavaClientGenerator();
        if (javaClientGenerator == null) {
            return null;
        }
        return new CriteriaJavaClientGenerator(getClientProject());
    }

    protected boolean shouldGenerateSchema() {
        String value = getStringProperty(TableProperty.GENERATE_SCHEMA);
        return Boolean.TRUE.equals(toBoolean(value));
    }

    public boolean enableSoftDelete() {
        String softDeleteColumnName = getStringProperty(TableProperty.SOFT_DELETE_COLUMN);
        String softDeleteValue = getStringProperty(TableProperty.NOT_DELETED_VALUE);
        Optional<IntrospectedColumn> columnOp = getColumn(softDeleteColumnName);

        return softDeleteColumnName != null && softDeleteValue != null && columnOp.isPresent();
    }

    public CustomColumnImpl getSoftDeleteColumn() {
        String softDeleteColumnName = getStringProperty(TableProperty.SOFT_DELETE_COLUMN);
        return (CustomColumnImpl) this.getColumn(softDeleteColumnName)
                .orElse(null);
    }

    protected static Boolean toBoolean(String value) {
        if (!StringUtility.stringHasValue(value)) {
            return null;
        }

        return "true".equalsIgnoreCase(value);
    }
    public String getStringProperty(TableProperty property) {
        return getStringProperty(property.getName());
    }

    protected String getStringProperty(String key) {
        String value = getTableConfigurationProperty(key);
        if (StringUtility.stringHasValue(value)) {
            return value;
        }
        return context.getProperty(key);
    }

    public FullyQualifiedJavaType getSchemaType() {
        String type = getBaseRecordType().replaceAll("DO$", "") + "Schema";
        return new FullyQualifiedJavaType(type);
    }

    @Override
    public List<IntrospectedColumn> getNonBLOBColumns() {
        return removeSoftDeleteColumn(super.getNonBLOBColumns());
    }

    @Override
    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        return removeSoftDeleteColumn(super.getNonPrimaryKeyColumns());
    }

    @Override
    public int getNonBLOBColumnCount() {
        int cnt = super.getNonBLOBColumnCount();
        if (enableSoftDelete()) {
            return cnt - 1;
        }
        return cnt;
    }

    @Override
    public List<IntrospectedColumn> getBaseColumns() {
        return removeSoftDeleteColumn(super.getBaseColumns());
    }

    @Override
    public List<IntrospectedColumn> getAllColumns() {
        return removeSoftDeleteColumn(super.getAllColumns());
    }

    protected List<IntrospectedColumn> removeSoftDeleteColumn(List<IntrospectedColumn> columns) {
        if (!enableSoftDelete()) {
            return columns;
        }

        CustomColumnImpl softDeleteColumn = getSoftDeleteColumn();
        return columns.stream()
                .filter(f -> !f.equals(softDeleteColumn))
                .collect(Collectors.toList());
    }

    public String getDeleteByConditionStatementId() {
        return "deleteByCondition";
    }

    public String getInsertBulkStatementId() {
        return "insertBulk";
    }

    public String getUpdateByConditionStatementId() {
        return "updateByCondition";
    }
}
