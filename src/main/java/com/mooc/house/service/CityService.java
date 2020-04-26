package com.mooc.house.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mooc.house.common.model.City;

@Service
public class CityService {
	//返回全局的city列表
	public List<City> getAllCity(){
		City city = new City();
		city.setCityCode("11000");
		city.setCityName("北京");
		city.setId(1);
		return Lists.newArrayList(city);
	}
}
