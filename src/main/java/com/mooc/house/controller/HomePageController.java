package com.mooc.house.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mooc.house.common.model.House;
import com.mooc.house.service.RecommandService;

@Controller
public class HomePageController {
	@Autowired
	private RecommandService recommandService;
	//推荐房源
	@RequestMapping("index")
	public String index(ModelMap modelMap) {
		//获取最新房源
		List<House> houses = recommandService.getLastest();
		modelMap.put("recomHouses", houses);
		return "homepage/index";
		
	}
	@RequestMapping("")
	public String home(ModelMap modeMap) {
		return "redirect:/index";
	}
	

}
