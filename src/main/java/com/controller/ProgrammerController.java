package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datasource.DataSourceType;
import com.entity.Programmer;
import com.mapper.ProgrammerMapper;

/**
 * @title ProgrammerController.java
 * @description TODO
 * @time 2019/07/18 16:42:06
 */
@RestController
public class ProgrammerController {
	@Autowired
	private ProgrammerMapper programmerDao;

	/**
	 * 查询全部数据源的数据
	 */
	@GetMapping("/db/programmers")
	public List<Programmer> getAllProgrammers() {
		List<Programmer> programmers = programmerDao.selectAll(DataSourceType.ONE);
		programmers.addAll(programmerDao.selectAll(DataSourceType.TWO));
		return programmers;
	}

	@GetMapping("ts/db/programmers")
	@Transactional
	public List<Programmer> getAllProgrammersTs() {
		List<Programmer> programmers = programmerDao.selectAll(DataSourceType.ONE);
		programmers.addAll(programmerDao.selectAll(DataSourceType.TWO));
		return programmers;
	}

	/**
	 * 不指定就使用默认的数据源
	 */
	@GetMapping("/db1/programmers")
	public List<Programmer> getDB1Programmers() {

		return programmerDao.selectAll(null);
	}

	/**
	 * 从指定数据源查询
	 */
	@GetMapping("/db2/programmers")
	public List<Programmer> getDB2Programmers() {
		return programmerDao.selectAll(DataSourceType.TWO);
	}
}
