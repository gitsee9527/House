package com.mooc.house.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mooc.house.common.model.User;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import com.mooc.house.mapper.AgencyMapper;

@Service
public class AgencyService {
	@Autowired
	private AgencyMapper agencyMapper;
	
	@Value("${file.prefix}")
	private String imgPrefix;
	/**
	 * 访问user获取详情
	 * 添加头像
	 * @param userId
	 * @return
	 */
	public User getAgentDetail(Long userId) {
		User user = new User();
		user.setId(userId);
		user.setType(2);
		System.out.println("PageParams.build(1, 1)--->"+PageParams.build(1, 1));
		List<User> list = agencyMapper.selectAgent(user, PageParams.build(1, 1));
		//添加用户头像
		setImg(list);
		if(!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	private void setImg(List<User> list) {
		list.forEach(i->{
			i.setAvator(imgPrefix+i.getAvator());
		});
	}
	public  PageData<User> getAllAgent(PageParams pageParams) {
		List<User> agents = agencyMapper.selectAgent(new User(), pageParams);
		setImg(agents);
		Long count = agencyMapper.selectAgentCount(new User());
		
		return PageData.buildpage(agents, count, pageParams.getPageSize(), pageParams.getPageNum());
	}

}
