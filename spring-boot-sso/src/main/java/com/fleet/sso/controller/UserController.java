package com.fleet.sso.controller;

import com.fleet.controller.BaseController;
import com.fleet.entity.user.User;
import com.fleet.json.R;
import com.fleet.user.service.UserService;
import com.fleet.util.MD5Util;
import com.fleet.util.UUIDUtil;
import com.fleet.util.jdbc.PageUtil;
import com.fleet.util.jdbc.entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@RequestMapping("/insert")
	public R insert(@RequestBody User user) throws Exception {
		try {
			String salt = UUIDUtil.getUUID();
			user.setUserSalt(salt);
			String password = MD5Util.encrypt(user.getUserPassword(), salt);
			user.setUserPassword(password);
			user.setUserRegTime(new Date());
			if (userService.insert(user)) {
				return R.ok();
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return R.error();
	}

	@RequestMapping("/delete")
	public R delete(@RequestBody User user) {
		userService.delete(user);
		return R.ok();
	}

	@RequestMapping("/update")
	public R update(@RequestBody User user) {
		if (user.getUserPassword() != null) {
			String salt = UUIDUtil.getUUID();
			user.setUserSalt(salt);
			String password = MD5Util.encrypt(user.getUserPassword(), salt);
			user.setUserPassword(password);
		}
		userService.update(user);
		return R.ok();
	}

	@RequestMapping("/get")
	public User get(@RequestBody User user) {
		return userService.get(user);
	}

	@RequestMapping("/list")
	public List<User> list(@RequestParam Map<String, Object> map) {
		return userService.list(map);
	}

	@RequestMapping("/listPage")
	public PageUtil<User> listPage(@RequestBody Page page) {
		PageUtil<User> pageUtil = userService.listPage(page);
		Page page2 = pageUtil.getPage();
		pageUtil.setPage(page2);
		System.out.println(page2);
		return pageUtil;
	}

}
