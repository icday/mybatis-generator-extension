package com.daiyc.instrument.mybatis.generator.extension.generator;

import com.daiyc.instrument.mybatis.generator.extension.base.CustomTableMyBatis3Impl;
import com.daiyc.instrument.mybatis.generator.extension.generator.provider.*;
import org.apache.commons.collections4.CollectionUtils;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.SqlProviderGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider.AbstractJavaProviderMethodGenerator;

import java.util.List;

import static com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes.BASE_SQL_PROVIDER;
import static com.daiyc.instrument.mybatis.generator.extension.generator.CriteriaLibTypes.SCHEMA_FACTORY;

/**
 * @author daiyc
 */
public class CriteriaSqlProviderGenerator extends SqlProviderGenerator {
    public CriteriaSqlProviderGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        List<CompilationUnit> answer = super.getCompilationUnits();

        if (CollectionUtils.isEmpty(answer)) {
            return answer;
        }

        TopLevelClass topLevelClass = (TopLevelClass) answer.get(0);
        addConstructor(topLevelClass);
        topLevelClass.setSuperClass(BASE_SQL_PROVIDER);
        topLevelClass.addImportedType(BASE_SQL_PROVIDER);

        addSelectByConditionMethod(topLevelClass);

        addUpdateByConditionMethod(topLevelClass);

        addInsertOrIgnoreMethod(topLevelClass);

        addUpsertMethod(topLevelClass);

        addInsertBulkMethod(topLevelClass);

        addDeleteByConditionMethod(topLevelClass);

        return answer;
    }

    @Override
    protected void addUpdateByPrimaryKeySelectiveMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractJavaProviderMethodGenerator methodGenerator =
                    new UpdateByIdSelectiveProviderMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    @Override
    protected void addInsertSelectiveMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaProviderMethodGenerator methodGenerator = new InsertSelectiveProviderMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void addInsertOrIgnoreMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaProviderMethodGenerator methodGenerator = new InsertSelectiveOrIgnoreProviderMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void addSelectByConditionMethod(TopLevelClass topLevelClass) {
        AbstractJavaProviderMethodGenerator methodGenerator = new SelectByConditionProviderMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addUpdateByConditionMethod(TopLevelClass topLevelClass) {
        AbstractJavaProviderMethodGenerator methodGenerator = new UpdateByConditionProviderMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addUpsertMethod(TopLevelClass topLevelClass) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaProviderMethodGenerator methodGenerator = new UpsertProviderMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, topLevelClass);
        }
    }

    protected void addInsertBulkMethod(TopLevelClass topLevelClass) {
        AbstractJavaProviderMethodGenerator methodGenerator = new InsertBulkSelectiveProviderMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addDeleteByConditionMethod(TopLevelClass topLevelClass) {
        AbstractJavaProviderMethodGenerator methodGenerator = new DeleteByConditionProviderMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addConstructor(TopLevelClass topLevelClass) {
        FullyQualifiedJavaType schemaType = ((CustomTableMyBatis3Impl) introspectedTable).getSchemaType();
        topLevelClass.addImportedType(schemaType);

        topLevelClass.addImportedType(SCHEMA_FACTORY);

        Method method = new Method(topLevelClass.getType().getShortName());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);

        method.addBodyLine(String.format("this.schema = %s.create(%s.class);", SCHEMA_FACTORY.getShortName(), schemaType.getShortName()));

        method.addBodyLine("this.tableName = \"" + introspectedTable.getFullyQualifiedTableNameAtRuntime() + "\";");
        topLevelClass.addMethod(method);
    }
}
