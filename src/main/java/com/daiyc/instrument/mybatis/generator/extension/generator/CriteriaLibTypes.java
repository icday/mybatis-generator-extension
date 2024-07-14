package com.daiyc.instrument.mybatis.generator.extension.generator;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author daiyc
 */
public class CriteriaLibTypes {
    public final static FullyQualifiedJavaType BASE_SQL_PROVIDER = new FullyQualifiedJavaType("com.daiyc.criteria.mybatis.BaseSqlProvider");

    public final static FullyQualifiedJavaType SCHEMA_FACTORY = new FullyQualifiedJavaType("com.daiyc.criteria.core.schema.SchemaFactory");

    public final static FullyQualifiedJavaType CONDITION = new FullyQualifiedJavaType("com.daiyc.criteria.core.model.Condition");

    public final static FullyQualifiedJavaType PAGINATION = new FullyQualifiedJavaType("com.daiyc.criteria.common.Pagination");

    public final static FullyQualifiedJavaType SINGLE_VALUE_TYPE = new FullyQualifiedJavaType("com.daiyc.criteria.core.schema.Value");
}
