package com.mooc.house.config;

import javax.servlet.Servlet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;

@Configuration
public class DruidConfig {
	//将外部文件与本文件数据关系进行绑定；也可以将bean方法返回的对象与外部的配置文件进行绑定
	@ConfigurationProperties(prefix = "spring.druid")
	@Bean(initMethod = "init",destroyMethod = "close")
	public DruidDataSource dataSource() {
		DruidDataSource druidDataSource =new DruidDataSource();
		druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
		return druidDataSource;
	}
	//打印出慢日志的druid的filter
	@Bean
	public Filter statFilter() {
		StatFilter statFilter = new StatFilter();
		statFilter.setSlowSqlMillis(1);
		//是否打印出慢日志
		statFilter.setLogSlowSql(true);
		//是否将日志合并起来
		statFilter.setMergeSql(true);
		return statFilter;
	}
	//添加监控，可以分析每个sql的执行时间，时间分布，执行操作记录数量的分布
	@Bean
	public ServletRegistrationBean<Servlet> servletRegistrationBean(){
		return new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
	}
	
}
