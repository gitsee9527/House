package com.mooc.house.common.model;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

public class User {
	private Long id;
	private String phone;
	private String name;
	private String email;
	private String passwd;
	//确认密码
	private String confirmPasswd;
	//用户/经纪人
	private Integer  type;
	//是否激活
	private Integer enable;
	//创建时间
	private Date createTime;
	//头像
	private String avator;
	//头像文件
	private MultipartFile avatarFile;
	//修改密码时
	private String newPassword;
	//激活链接有个key
	private String key;
	//经纪机构ID
	private Long agencyid;
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String password) {
		this.passwd = password;
	}
	public String getConfirmPasswd() {
		return confirmPasswd;
	}
	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAvator() {
		return avator;
	}
	public void setAvator(String avator) {
		this.avator = avator;
	}
	public MultipartFile getAvatarFile() {
		return avatarFile;
	}
	public void setAvatarFile(MultipartFile avatorFile) {
		this.avatarFile = avatorFile;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Long getAgencyid() {
		return agencyid;
	}
	public void setAgencyid(Long agencyid) {
		this.agencyid = agencyid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
