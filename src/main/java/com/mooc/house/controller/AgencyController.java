package com.mooc.house.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mooc.house.common.model.House;
import com.mooc.house.common.model.User;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import com.mooc.house.service.AgencyService;
import com.mooc.house.service.HouseService;

@Controller
public class AgencyController {
	@Autowired
	public AgencyService agencyService;
	@Autowired
	public HouseService houseService;
	@RequestMapping("/agency/agentList")
	public String agentList(Integer pageSize, Integer pageNum,ModelMap modelMap) {
		PageData<User> ps =agencyService.getAllAgent(PageParams.build(pageSize,pageNum));
		modelMap.put("ps", ps);
		return "/user/agent/agentList";
	}
	
	@RequestMapping("agency/agentDetail")
	public String agentDetail(Long id,ModelMap modelMap) {
		User user = agencyService.getAgentDetail(id);
		House query = new House();
		query.setUserId(id);
		query.setBookmarked(false);
		PageData<House> bindHouse = houseService.queryHouse(query, new PageParams(3,1));
		if(bindHouse!=null) {
			modelMap.put("bindHouses", bindHouse.getList());
		}
		modelMap.put("agent", user);
		return "/user/agent/agentDetail";
		
	}
}
