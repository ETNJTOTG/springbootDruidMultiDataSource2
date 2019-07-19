package com.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @title DataSourceAspect.java
 * @description 更换数据源
 * @time 2019/07/17 14:59:08
 */
@Aspect
@Component
@Slf4j
public class DataSourceAspect {

	@Pointcut(value = "execution(* com.mapper.*.*(..))")
	public void dataSourcePointCut() {
	}

	@Before(value = "dataSourcePointCut()")
	public void beforeSwitchDS(JoinPoint point) {
		Object[] args = point.getArgs();
		if (args == null || args.length < 1 || !DataSourceType.TWO.equals(args[0])) {
			log.info("当前数据源: " + DataSourceType.ONE);
			DataSourceContextHolder.setDataSourceType(DataSourceType.ONE);
		} else {
			log.info("当前数据源: " + DataSourceType.TWO);
			DataSourceContextHolder.setDataSourceType(DataSourceType.TWO);
		}
	}

	@After(value = "dataSourcePointCut()")
	public void afterSwitchDS(JoinPoint point) {
		DataSourceContextHolder.clearDataSourceType();
	}

}
