package com.mooc.house.filter;

import java.util.ArrayList;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.core.filter.Filter;

@Configuration
public class FilterBeanConfig {
	/**
	 * 1、构造filter
	 * 2、配置拦截 URLPattern
	 * 3、利用 FilterRegistrationBean 进行包装
	 * @return
	 */
	@Bean
	public FilterRegistrationBean logFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new LogFilter());
		ArrayList<String> urList = new ArrayList<String>();
		urList.add("**");
		filterRegistrationBean.setUrlPatterns(urList);
		return filterRegistrationBean;
	}
//	public FilterRegistrationBean logFilter() {
//		
//	}
}

