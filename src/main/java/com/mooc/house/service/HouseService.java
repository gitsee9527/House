package com.mooc.house.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mooc.house.common.constants.HouseUserType;
import com.mooc.house.common.model.Community;
import com.mooc.house.common.model.House;
import com.mooc.house.common.model.HouseUser;
import com.mooc.house.common.model.User;
import com.mooc.house.common.model.UserMsg;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import com.mooc.house.common.utils.BeanHelper;
import com.mooc.house.mapper.HouseMapper;

@Service
public class HouseService {
	@Value("${file.prefix}")
	private String imgPrefix;
	
	@Autowired
	private HouseMapper houseMapper;
	@Autowired
	private FileService fileService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private MailService mailSerice;
	/**
	 * 1、查询小区
	 * 2、添加图片服务地址前缀
	 * 3、构建分页结果
	 * @param query
	 * @param build
	 */
	public PageData<House> queryHouse(House query, PageParams pageParams) {
		List<House> houses = Lists.newArrayList();
		if(!Strings.isNullOrEmpty(query.getName())) {
			Community community = new Community();
			community.setName(query.getName());
			List<Community> communities = houseMapper.selectCommunity(community);
			if(!communities.isEmpty()) {
				query.setCommunityId(communities.get(0).getId());
			}
		}
		houses = queryAndSetImg(query,pageParams);
		Long count = houseMapper.selectPageCount(query);
		return PageData.buildpage(houses, count, pageParams.getPageSize(), pageParams.getPageNum());
	}

	public List<House> queryAndSetImg(House query, PageParams pageParams) {
		System.out.println("-----------"+query.toString());
		System.out.println("-----------"+pageParams.toString());
		List<House> houses = houseMapper.selectPageHouses(query, pageParams);
		houses.forEach(h->{
			h.setFirstImg(imgPrefix+h.getFirstImg());
			h.setImageList(h.getImageList().stream().map(img->imgPrefix+img).collect(Collectors.toList()));
			h.setFloorPlanList(h.getFloorPlanList().stream().map(pic->imgPrefix+pic).collect(Collectors.toList()));
			
		});
		return houses;
	}
	
	public HouseUser getHouseUser(Long houseId){
		HouseUser houseUser =  houseMapper.selectSaleHouseUser(houseId);
		return houseUser;
	}
	public House queryOneHouser(Long id) {
		House query = new House();
		query.setId(id);
		List<House> house = queryAndSetImg(query, PageParams.build(1, 1));
		if(!house.isEmpty()) {
			return house.get(0);
		}
		return null;
	}

	public void addUserMsg(UserMsg userMsg) {
		BeanHelper.onInsert(userMsg);
		houseMapper.insertUserMsg(userMsg);
		User agent = agencyService.getAgentDetail(userMsg.getAgentId());
		mailSerice.sendMail("来自用户"+userMsg.getEmail(), userMsg.getMsg(), agent.getEmail());
		
	}

	public List<Community> getAllCommunitys() {
		Community community = new Community();
		return houseMapper.selectCommunity(community);
	}
	/**
	 * 1/添加房产图片
	 * 2/添加户型图
	 * 3/插入房产信息
	 * 4/绑定用户到房产关系
	 * @param house
	 * @param user
	 */
	public void addHouse(House house, User user) {
		if(CollectionUtils.isNotEmpty(house.getHouseFiles())) {
			String images = Joiner.on(",").join(fileService.getImgPath(house.getHouseFiles()));
			house.setImages(images);
		}
		if(CollectionUtils.isNotEmpty(house.getFloorPlanFiles())) {
			String images= Joiner.on(",").join(fileService.getImgPath(house.getFloorPlanFiles()));
			house.setFloorPlan(images);
		}
		BeanHelper.onInsert(house);
		houseMapper.insert(house);
		//绑定用户和房产的关系 false --不收藏，添加
		bindUser2House(house.getId(),user.getId(),false);
	}

	public void bindUser2House(Long houseId, Long userId, boolean isCollect) {
		HouseUser exisHouseUser = houseMapper.selectHouseUser(userId,houseId,isCollect?HouseUserType.BOOKNARK.value:HouseUserType.SALE.value);
		if(exisHouseUser!=null) {
			return ;
		}
		HouseUser houseUser = new HouseUser();
		houseUser.setHouseId(houseId);
		houseUser.setUserId(userId);
		houseUser.setType(isCollect?HouseUserType.BOOKNARK.value:HouseUserType.SALE.value);
		//直接修改创建时间
		BeanHelper.setDefaultProp(houseUser, HouseUser.class);
		BeanHelper.onInsert(houseUser);
		houseMapper.insertHouseUser(houseUser);
		
	}

	public void updateRating(Long id, Double rating) {
		//此时可以先判断rating是否大于5，如果大于5则为非法请求
		House house = queryOneHouser(id);
		Double oldRating = house.getRating();
		Double newRating = oldRating.equals(0D)? rating : Math.min((oldRating+rating)/2, 5);
		House updateHouse = new House();
		updateHouse.setId(id);
		updateHouse.setRating(newRating);
		BeanHelper.onUpdate(updateHouse);
		houseMapper.updateHouse(updateHouse);
	}

	public void unBindUser2House(Long id, Long userId, HouseUserType type ) {
		houseMapper.deletehouseUser(id,userId,type.value);
	}

}
