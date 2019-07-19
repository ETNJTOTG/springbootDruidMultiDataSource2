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
public class XATransactionController {

	@Autowired
	private ProgrammerMapper programmerDao;

	@RequestMapping("/db/change")
	@Transactional
	public void changeDb() {
		Programmer programmer01 = new Programmer(1, "db1", 100, 6662.32f, new Date());
		Programmer programmer02 = new Programmer(1, "db2", 100, 6662.32f, new Date());
		programmerDao.modify(DataSourceType.ONE, programmer01);
		programmerDao.modify(DataSourceType.TWO, programmer02);
	}

	@RequestMapping("ts/db/change")
	@Transactional
	public void changeTsDb() {
		Programmer programmer01 = new Programmer(1, "db1", 99, 6662.32f, new Date());
		Programmer programmer02 = new Programmer(1, "db2", 99, 6662.32f, new Date());
		programmerDao.modify(DataSourceType.ONE, programmer01);
		programmerDao.modify(DataSourceType.TWO, programmer02);
		System.out.println(1 / 0);
	}

}
