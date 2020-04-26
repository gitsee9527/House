package com.mooc.house.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import com.mooc.house.common.utils.HashUtils;
import com.mooc.house.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

//  @Autowired
//  private AgencyService agencyService;
	/**
	 * 注册提交：1注册验证 2 发送邮件 3 验证失败重定向到注册页面 注册页获取：根据account对象为依据判断是否注册页面获取请求
	 * 
	 * @param account
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("accounts/register")
	public String accountRegister(User account, ModelMap modelMap) {
		if (account == null || account.getName() == null) {
//		  modelMap.put("agencyList",  agencyService.getAllAgency());
			return "/user/accounts/register";
		}
		// 用户验证
		ResultMsg resultMsg = UserHelper.validate(account);
		System.out.println("resultMsg-->"+resultMsg);
		if (resultMsg.isSuccess() && userService.addAccount(account)) {
			modelMap.put("email", account.getEmail());
			return "/user/accounts/registerSubmit";
		} else {
			// 重定向让用户重新去填写
			return "redirect:/account/register?" + resultMsg.asUrlParams();
		}
	}

	@RequestMapping("accounts/verify")
	public String verify(String key) {
		boolean result = userService.enable(key);

		if (result) {
			return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
		} else {
			return "redircet:/accounts/redister?" + ResultMsg.errorMsg("激活失败！请确认链接是否过期");
		}
	}

	// --------------------------登录流程--------------------------------------
	/**
	 * 登录接口
	 */
	@RequestMapping("/accounts/signin")
	public String signin(HttpServletRequest req) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String target = req.getParameter("target");
		if (username == null || password == null) {
			req.setAttribute("target", target);
			return "/user/accounts/signin";
		}
		User auth = userService.auth(username, password);
		// 验证成功将user放到session里
		if (auth == null) {
			return "redirect:/accounts/signin?" + "target=" + target + "$username=" + username + "&"
					+ ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
		}else {
			//强制创建一个session
			HttpSession session = req.getSession(true);
			session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE, auth);
			session.setAttribute(CommonConstants.USER_ATTRIBUTE, auth);
			return StringUtils.isNotBlank(target)?"redirect:"+target:"redirect:/index";
		}
	}
	
	/**
	 * 登出，就是让用户信息删除的过程
	 */
	@RequestMapping("accounts/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//session 注销
		session.invalidate();
		//重定向到首页
		return "redirect:/index";
	}
	
	
	//--------------------个人信息页----------------------------
	/**
	 * 1、提供个人信息页面
	 * 2、更新用户信息
	 * @param updateUser
	 * @return
	 */
	@RequestMapping("accounts/profile")
	public String profile(HttpServletRequest request, User updateUser,ModelMap model) {
		//根据email是否为空来判断用户的操作
		if(updateUser.getEmail()==null) {
			//返回个人信息页
			return "/user/accounts/profile";
		}
		userService.updateUser(updateUser,updateUser.getEmail());
		User query = new User();
		query.setEmail(updateUser.getEmail());
		List<User> userByQuery = userService.getUserByQuery(query);
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstants.USER_ATTRIBUTE, userByQuery.get(0));
		return "redirect:/accounts/profile?"+ResultMsg.successMsg("更新成功").asUrlParams();
	}
	
	/**
	 * 更新密码
	 */
	@RequestMapping("accounts/changePassword")
	public String changePassword(String email,String password,String newPassword,String confirmPassword,ModelMap modelmap) {
		User user = userService.auth(email, password);
		if(user==null || !confirmPassword.equals(newPassword) ){
			return "redirect:/accounts/profile?"+ResultMsg.errorMsg("密码错误").asUrlParams();
		}
		User updateUser = new User();
		updateUser.setPasswd(HashUtils.encyPassword(newPassword));
		userService.updateUser(updateUser,updateUser.getEmail());
		return "redirect:/accounts/profile?"+ResultMsg.successMsg("更新成功").asUrlParams();
	}

}
