package com.mooc.house.controller;

import com.alibaba.druid.util.StringUtils;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;

public class UserHelper {
	public static ResultMsg validate(User account) {
		if(StringUtils.isEmpty(account.getEmail())) {
			return ResultMsg.errorMsg("Email有误");
		}
		if(StringUtils.isEmpty(account.getName()) ){
			return ResultMsg.errorMsg("名字有误");
		}
		if(StringUtils.isEmpty(account.getConfirmPasswd()) ||StringUtils.isEmpty(account.getPasswd())|| 
				!account.getPasswd().equals(account.getConfirmPasswd())){
			System.out.println("getConfirmPasswd"+account.getConfirmPasswd()+"getPassword"+account.getPasswd());
			return ResultMsg.errorMsg("密码有误");
		}
		if(account.getPasswd().length()<6) {
			return ResultMsg.errorMsg("密码大于6位");
		}
		return ResultMsg.successMsg("");
	}
}
