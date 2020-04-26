package com.mooc.house.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.constants.HouseUserType;
import com.mooc.house.common.model.House;
import com.mooc.house.common.model.HouseUser;
import com.mooc.house.common.model.User;
import com.mooc.house.common.model.UserMsg;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import com.mooc.house.common.result.ResultMsg;
import com.mooc.house.interceptor.userContext;
import com.mooc.house.service.AgencyService;
import com.mooc.house.service.CityService;
import com.mooc.house.service.HouseService;
import com.mooc.house.service.RecommandService;

@Controller
public class HouseController {
	@Autowired
	private HouseService houseService;
	@Autowired
	private CityService cityService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private RecommandService recommandService;
	/**
	 *  * 1、分页
	 * 2、支持小区搜索、类型搜索
	 * 3、支持排序
	 * 4、支持展示图片、价格、标题、地址等信息
	 * @param pageSize  
	 * @param pageNum    分页查询
	 * @param query      包含出租或售卖类型，小区名字或房屋的描述进行模糊匹配
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/house/list")
	public String houserList(Integer pageSize,Integer  pageNum, House query,ModelMap modelMap) {
		PageData<House> ps = houseService.queryHouse(query,PageParams.build(pageSize, pageNum));
		List<House> hotHouse = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
		modelMap.put("recomHouses", hotHouse);
		modelMap.put("ps", ps);
		modelMap.put("vo", query);
		
		return "house/listing";
	}
	
	/**
	 * 查询房屋详情
	 * 查询关联经纪人
	 * @param id
	 * @return
	 */
	@RequestMapping("house/detail")
	public String houseDetail(Long id, ModelMap modelMap) {
		House house = houseService.queryOneHouser(id);
		HouseUser houseUser = houseService.getHouseUser(id);
		//点击
		recommandService.increase(id);
		if(houseUser.getUserId()!=null && !houseUser.getUserId().equals(0)) {
			modelMap.put("agent", agencyService.getAgentDetail(house.getUserId()));
		}
		List<House> hotHouse = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
		modelMap.put("recomHouses", hotHouse);
		modelMap.put("house", house);
		return "/house/detail";
	}
	/**
	 * 添加留言
	 * @param userMsg
	 * @return
	 */
	@RequestMapping("house/leaveMsg")
	public String houseMsa(UserMsg userMsg) {
		houseService.addUserMsg(userMsg);
		return "redirect:/house/detail?id"+userMsg.getHouseId();
	}
	
	/**
	 * 返回添加房产的页面
	 */
	@RequestMapping("house/toAdd")
	public String toAdd(ModelMap map) {
		map.put("citys", cityService.getAllCity());
		map.put("communitys", houseService.getAllCommunitys());
		return "house/add";
	}
	/**
	 * 1/获取用户
	 * 2/设置房产状态
	 * 3/添加房产
	 * @param modelmap
	 * @return
	 */
	@RequestMapping("/house/add")
	public String doAdd(House house) {
		User user = userContext.getUser();
		house.setState(CommonConstants.HOUSE_STATE_UP);
		//添加房产
		houseService.addHouse(house,user);
		return "redircet:/house/ownlist";
		
	}
	
	@RequestMapping("house/ownlist")
	public String ownlist(House house, Integer pageNum, Integer pageSize , ModelMap modelMap) {
		//获取当前的用户
		User user = userContext.getUser();
		house.setUserId(user.getId());
		house.setBookmarked(false);
		modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
		modelMap.put("pageType", "own");
		return "house/ownlist";
	}
	//评分
	@ResponseBody
	@RequestMapping("house/rating")
	public ResultMsg houseRete( Double rating, Long id) {
		houseService.updateRating(id,rating);
		return ResultMsg.successMsg("ok");
	}

	
	
	//收藏
	@ResponseBody
	@RequestMapping("house/bookmark")
	public ResultMsg bookmark(Long id) {
		User user = userContext.getUser();
		houseService.bindUser2House(id, user.getId(), true);
		return ResultMsg.successMsg("ok");
	}
	
	//删除收藏
	
	@ResponseBody
	@RequestMapping("house/unbookmark")
	public ResultMsg unbookmark(Long id) {
		User user = userContext.getUser();
		houseService.unBindUser2House(id, user.getId(), HouseUserType.BOOKNARK);
		return ResultMsg.successMsg("ok");
	}
	@RequestMapping("house/del")
	public String delsale(Long id,String pageType) {
		User user = userContext.getUser();
		houseService.unBindUser2House(id, user.getId(), pageType.equals("own")?HouseUserType.SALE:HouseUserType.BOOKNARK);
		return "redircet:/house/ownlist";
	}
	
	//收藏列表
	@RequestMapping("house/bookmarked")
	public String bookmarked(House house, Integer pageNum,Integer pageSize,ModelMap modelMap) {
		User user = userContext.getUser();
		house.setBookmarked(true);
		house.setUserId(user.getId());
		modelMap.put("ps", houseService.queryHouse(house, PageParams.build(pageSize, pageNum)));
		modelMap.put("pageType", "book");
		return "/house/ownlist";
	}
}
