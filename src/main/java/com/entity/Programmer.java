package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @title ProgrammerMapper.java
 * @description TODO
 * @time 2019/07/18 16:37:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName
public class Programmer {
	@TableId(type = IdType.AUTO)
	private int id;

	private String name;

	private int age;

	private float salary;

	private Date birthday;

}
