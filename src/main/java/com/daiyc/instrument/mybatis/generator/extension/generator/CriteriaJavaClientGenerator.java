package com.daiyc.instrument.mybatis.generator.extension.generator;

import com.daiyc.instrument.mybatis.generator.extension.generator.client.*;
import org.apache.commons.collections4.CollectionUtils;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.List;

/**
 * @author daiyc
 */
public class CriteriaJavaClientGenerator extends AnnotatedClientGenerator {
    public CriteriaJavaClientGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        List<CompilationUnit> compilationUnits = super.getCompilationUnits();
        if (CollectionUtils.isEmpty(compilationUnits)) {
            return compilationUnits;
        }

        processJavaClientClass((Interface) compilationUnits.get(0));
        return compilationUnits;
    }

    protected void processJavaClientClass(Interface topLevelClass) {
        addSelectByConditionMethod(topLevelClass);

        addUpdateByIdJavaMapperMethod(topLevelClass);

        addUpdateByConditionMethod(topLevelClass);

        addUpsertMethod(topLevelClass);

        addInsertOrIgnoreMethod(topLevelClass);

        addInsertBulkMethod(topLevelClass);

        addDeleteByConditionJavaMapperMethod(topLevelClass);
    }

    protected void addInsertBulkMethod(Interface interfaze) {
        AbstractJavaMapperMethodGenerator generator = new InsertBulkSelectiveJavaMapperMethodGenerator();
        initializeAndExecuteGenerator(generator, interfaze);
    }

    protected void addUpsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaMapperMethodGenerator generator = new UpsertJavaMapperMethodGenerator();
            initializeAndExecuteGenerator(generator, interfaze);
        }
    }

    protected void addInsertOrIgnoreMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsertSelective()) {
            AbstractJavaMapperMethodGenerator generator = new InsertSelectiveOrIgnoreJavaMapperMethodGenerator();
            initializeAndExecuteGenerator(generator, interfaze);
        }
    }

    protected void addSelectByConditionMethod(Interface interfaze) {
        AbstractJavaMapperMethodGenerator generator = new SelectByConditionJavaMapperMethodGenerator();
        initializeAndExecuteGenerator(generator, interfaze);
    }

    protected void addUpdateByConditionMethod(Interface interfaze) {
        AbstractJavaMapperMethodGenerator generator = new UpdateByConditionJavaMapperMethodGenerator();
        initializeAndExecuteGenerator(generator, interfaze);
    }

    protected void addUpdateByIdJavaMapperMethod(Interface interfaze) {
        AbstractJavaMapperMethodGenerator generator = new UpdateByIdDelegateJavaMapperMethodGenerator();
        initializeAndExecuteGenerator(generator, interfaze);
    }

    protected void addDeleteByConditionJavaMapperMethod(Interface interfaze) {
        AbstractJavaMapperMethodGenerator generator = new DeleteByConditionJavaMapperMethodGenerator();
        initializeAndExecuteGenerator(generator, interfaze);
    }

    @Override
    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByIdJavaMapperMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectByIdJavaMapperMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        super.addUpdateByPrimaryKeySelectiveMethod(interfaze);
    }

    @Override
    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            AbstractJavaMapperMethodGenerator methodGenerator =
                    new UpdateByIdWithBLOBsJavaMapperMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    protected void addInsertSelectiveMethod(Interface interfaze) {
        super.addInsertSelectiveMethod(interfaze);
    }

    @Override
    public List<CompilationUnit> getExtraCompilationUnits() {
        CriteriaSqlProviderGenerator sqlProviderGenerator = new CriteriaSqlProviderGenerator(getProject());
        sqlProviderGenerator.setContext(context);
        sqlProviderGenerator.setIntrospectedTable(introspectedTable);
        sqlProviderGenerator.setProgressCallback(progressCallback);
        sqlProviderGenerator.setWarnings(warnings);
        return sqlProviderGenerator.getCompilationUnits();
    }
}
