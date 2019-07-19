package com.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * @title MySqlSessionTemplate.java
 * @description TODO
 * @time 2019/07/19 08:44:47
 */
@Slf4j
public class MySqlSessionTemplate extends SqlSessionTemplate {

	private final SqlSessionFactory sqlSessionFactory;
	private final ExecutorType executorType;
	private final PersistenceExceptionTranslator exceptionTranslator;
	private final SqlSession sqlSessionProxy;
	private SqlSessionFactory defaultTargetSqlSessionFactory;
	private Map<Object, SqlSessionFactory> targetSqlSessionFactories;

	public MySqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
	}

	public MySqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
		this(sqlSessionFactory, executorType, new MyBatisExceptionTranslator(
				sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true));
	}

	public MySqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,
			PersistenceExceptionTranslator exceptionTranslator) {
		super(sqlSessionFactory, executorType, exceptionTranslator);
		this.sqlSessionFactory = sqlSessionFactory;
		this.executorType = executorType;
		this.exceptionTranslator = exceptionTranslator;
		this.sqlSessionProxy = (SqlSession) Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(),
				new Class[] { SqlSession.class }, new SqlSessionInterceptor());
		this.defaultTargetSqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public SqlSessionFactory getSqlSessionFactory() {
		String dataSourceKey = DataSourceContextHolder.getDataSourceType();
		log.info("当前会话工厂 : {}", dataSourceKey);
		SqlSessionFactory targetSqlSessionFactory = targetSqlSessionFactories.get(dataSourceKey);
		if (targetSqlSessionFactory != null) {
			return targetSqlSessionFactory;
		} else if (defaultTargetSqlSessionFactory != null) {
			return defaultTargetSqlSessionFactory;
		} else {
			Assert.notNull(targetSqlSessionFactories,
					"Property 'targetSqlSessionFactories' or 'defaultTargetSqlSessionFactory' are required");
			Assert.notNull(defaultTargetSqlSessionFactory,
					"Property 'defaultTargetSqlSessionFactory' or 'targetSqlSessionFactories' are required");
		}
		return this.sqlSessionFactory;
	}

	@Override
	public <T> T selectOne(String statement) {
		return this.sqlSessionProxy.<T>selectOne(statement);
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionProxy.<T>selectOne(statement, parameter);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, parameter, mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.sqlSessionProxy.<K, V>selectMap(statement, parameter, mapKey, rowBounds);
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement) {
		return this.sqlSessionProxy.selectCursor(statement);
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
		return this.sqlSessionProxy.selectCursor(statement, parameter);
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
	}

	@Override
	public <E> List<E> selectList(String statement) {
		return this.sqlSessionProxy.<E>selectList(statement);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionProxy.<E>selectList(statement, parameter);
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionProxy.<E>selectList(statement, parameter, rowBounds);
	}

	@Override
	public void select(String statement, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, handler);
	}

	@Override
	public void select(String statement, Object parameter, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, handler);
	}

	@Override
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
	}

	@Override
	public int insert(String statement) {
		return this.sqlSessionProxy.insert(statement);
	}

	@Override
	public int insert(String statement, Object parameter) {
		return this.sqlSessionProxy.insert(statement, parameter);
	}

	@Override
	public int update(String statement) {
		return this.sqlSessionProxy.update(statement);
	}

	@Override
	public int update(String statement, Object parameter) {
		return this.sqlSessionProxy.update(statement, parameter);
	}

	@Override
	public int delete(String statement) {
		return this.sqlSessionProxy.delete(statement);
	}

	@Override
	public int delete(String statement, Object parameter) {
		return this.sqlSessionProxy.delete(statement, parameter);
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		return getConfiguration().getMapper(type, this);
	}

	@Override
	public void commit() {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	@Override
	public void commit(boolean force) {
		throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
	}

	@Override
	public void rollback() {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	@Override
	public void rollback(boolean force) {
		throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
	}

	@Override
	public void close() {
		throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
	}

	@Override
	public void clearCache() {
		this.sqlSessionProxy.clearCache();
	}

	@Override
	public Configuration getConfiguration() {
		return this.sqlSessionFactory.getConfiguration();
	}

	@Override
	public Connection getConnection() {
		return this.sqlSessionProxy.getConnection();
	}

	@Override
	public List<BatchResult> flushStatements() {
		return this.sqlSessionProxy.flushStatements();
	}

	@Override
	public void destroy()
	// throws Exception
	{

	}

	private class SqlSessionInterceptor implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// 在getSqlSession传参时候，用我们重写的getSqlSessionFactory获取当前数据源对应的会话工厂
			final SqlSession sqlSession = SqlSessionUtils.getSqlSession(
					MySqlSessionTemplate.this.getSqlSessionFactory(), MySqlSessionTemplate.this.executorType,
					MySqlSessionTemplate.this.exceptionTranslator);
			try {
				Object result = method.invoke(sqlSession, args);
				if (!SqlSessionUtils.isSqlSessionTransactional(sqlSession,
						MySqlSessionTemplate.this.getSqlSessionFactory())) {
					sqlSession.commit(true);
				}
				return result;
			} catch (Throwable t) {
				Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
				if (MySqlSessionTemplate.this.exceptionTranslator != null
						&& unwrapped instanceof PersistenceException) {
					Throwable translated = MySqlSessionTemplate.this.exceptionTranslator
							.translateExceptionIfPossible((PersistenceException) unwrapped);
					if (translated != null) {
						unwrapped = translated;
					}
				}
				throw unwrapped;
			} finally {
				SqlSessionUtils.closeSqlSession(sqlSession, MySqlSessionTemplate.this.getSqlSessionFactory());
			}
		}
	}

	public void setTargetSqlSessionFactories(Map<Object, SqlSessionFactory> targetSqlSessionFactories) {
		this.targetSqlSessionFactories = targetSqlSessionFactories;
	}

	public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {
		this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;
	}

	public ExecutorType getExecutorType() {
		return this.executorType;
	}

	public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
		return this.exceptionTranslator;
	}

}
