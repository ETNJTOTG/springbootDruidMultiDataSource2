package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datasource.DataSourceType;
import com.entity.Programmer;
import com.mapper.ProgrammerMapper;

import java.util.Date;

/**
 * @title ProgrammerController.java
 * @description TODO
 * @time 2019/07/18 16:42:06
 */
@RestController
public class TransactionController {

	@Autowired
	private ProgrammerMapper programmerDao;

	@RequestMapping("db1/change")
	@Transactional
	public void changeDb1() {
		Programmer programmer = new Programmer(1, "db1", 99, 6662.32f, new Date());
		programmerDao.modify(DataSourceType.ONE, programmer);
	}

	@RequestMapping("ts/db1/change")
	@Transactional
	public void changeTsDb1() {
		Programmer programmer = new Programmer(1, "db1", 88, 6662.32f, new Date());
		programmerDao.modify(DataSourceType.ONE, programmer);
		// 抛出异常 查看回滚
		System.out.println(1 / 0);
	}

}
