package com.mooc.house.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mooc.house.common.model.User;
import com.mooc.house.common.utils.BeanHelper;
import com.mooc.house.common.utils.HashUtils;
import com.mooc.house.mapper.UserMapper;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private MailService mailService;
	
	public List<User> getUsers(){
		return userMapper.selectUsers();
	}
	@Value("${file.prefix}")
	private String imgPrefix;
	/**
	 * 1/插入数据库，非激活，密码加盐MD5；保存头像到本地
	 * 2/生产key，绑定email
	 * 3/发送邮件给用户
	 * @param account
	 * @return
	 */
	//受检异常和非受检异常
	@Transactional(rollbackFor = Exception.class)
	public boolean addAccount(User account) {
		try {
			account.setPasswd(HashUtils.encyPassword(account.getPasswd()));
			System.out.println("------11-----"+account.getAvatarFile());
			List<String> imgPath = fileService.getImgPath(Lists.newArrayList(account.getAvatarFile()));
			System.out.println("------22-----"+imgPath);
			
			if(!imgPath.isEmpty()) {
				account.setAvator(imgPath.get(0));
			}
			BeanHelper.setDefaultProp(account, User.class);
			BeanHelper.onInsert(0);
			account.setEnable(0);
			userMapper.insert(account);
			mailService.registerNotify(account.getEmail());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean enable(String key) {
		
		return mailService.enable(key);
	}
	//用户名和密码的验证
	public User auth(String username, String password) {
		User user = new User();
		user.setEmail(username);
		user.setPasswd(password);
		user.setEnable(1);
		List<User> list = getUserByQuery(user);
		if(!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	public List<User> getUserByQuery(User user) {
		List<User> list = userMapper.selectUserByQuery(user);
		//对头像 的路径处理
		list.forEach(u->{
			u.setAvator(imgPrefix+u.getAvator());
		});
		return list;
	}
	
	public void updateUser(User updateUser, String email) {
		updateUser.setEmail(email);
		BeanHelper.onUpdate(updateUser);
		userMapper.update(updateUser);
	}
	
	


}
