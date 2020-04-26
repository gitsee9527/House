package com.mooc.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mooc.house.common.model.HouseUser;
import com.mooc.house.common.model.User;
//添加mapper注解，使mybatis的起步依赖扫描到UserMapper
@Mapper
public interface UserMapper {
	public  List <User> selectUsers();

	public int insert(User account);

	public int delete(String email);

	public int update(User updateUser);

	public List<User> selectUserByQuery(User user);
	
}
