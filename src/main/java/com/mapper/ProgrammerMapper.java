package com.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Programmer;

/**
 * @title ProgrammerMapper.java
 * @description TODO
 * @time 2019/07/18 16:37:41
 */
@Mapper
public interface ProgrammerMapper 
	//extends BaseMapper<Programmer>
{
	//@Select("select * from programmer")
	List<Programmer> selectAll(String dataSource);

	//@Insert("insert into programmer (name, age, salary, birthday) VALUES (#{name}, #{age}, #{salary}, #{birthday})")
	void save(Programmer programmer);

	//@Select("select * from programmer where name = #{id}")
	Programmer selectById(int id);

	//@Update("update programmer set name=#{pro.name},age=#{pro.age},salary=#{pro.salary},birthday=#{pro.birthday} where id=#{pro.id}")
	int modify(String dataSource, @Param("pro") Programmer programmer);

	//@Delete("delete from programmer where id = #{id}")
	void delete(int id);
}
