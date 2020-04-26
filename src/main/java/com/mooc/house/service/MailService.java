package com.mooc.house.service;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mooc.house.common.model.User;
import com.mooc.house.mapper.UserMapper;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${domain.name}")
	private String domainName;
	
	@Value("${spring.mail.username}")
	private String from;
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 最大存储空间100，同时注册超过100则剔除，
	 * 设置超时时间，超过15分钟未激活则二次注册,
	 * 超时还未发激活链接，将用户从数据库中删除，防止下次注册造成唯一性冲突
	 */
	private final Cache<String,String> registerCache = CacheBuilder.newBuilder()
			.maximumSize(100).expireAfterAccess(15, TimeUnit.MINUTES)
			.removalListener(new RemovalListener<String, String>() {
				public void onRemoval(RemovalNotification<String,String> notification) {
					//过期后删除
					userMapper.delete(notification.getValue());
				}
			}).build();
	
	public void sendMail(String title,String url,String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(email);
		message.setText(url);
		javaMailSender.send(message);
	}
	
	/**
	 * 1、缓存key-email的关系
	 * 2、借助spring email发送邮件
	 * 3.借助异步框架进行异步操作
	 * @param email
	 */
	@Async//Spring 会启动一个线程池来异步调用,使用时同时需要在启动类添加@EnableAsync
	public  void registerNotify(String email) {
		String randomKey = RandomStringUtils.randomAlphabetic(10);
		registerCache.put(randomKey, email);
		//发送邮件，构造激活链接 
		String url = "http://"+domainName+"/accounts/verify?key="+randomKey;
		sendMail("房产激活邮件",url,email);
	}
	//验证email是否合法
	public boolean enable(String key) {
		String email = registerCache.getIfPresent(key);
		if(StringUtils.isBlank(email)) {
			return false;
		}
		User updateUser = new User();
		updateUser.setEmail(email);
		updateUser.setEnable(1);
		userMapper.update(updateUser);
		registerCache.invalidate(key);
		return true;
	}
}
