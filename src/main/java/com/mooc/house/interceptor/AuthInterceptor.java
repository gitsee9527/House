package com.mooc.house.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;

@Component
public class AuthInterceptor implements HandlerInterceptor{
	@Override
	public  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		if(requestURI.startsWith("/static")|| requestURI.startsWith("/error")) {
			return true;
		}
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(CommonConstants.USER_ATTRIBUTE);
		if(user!=null) {
			userContext.setUser(user);
		}
		return true;
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}
}
