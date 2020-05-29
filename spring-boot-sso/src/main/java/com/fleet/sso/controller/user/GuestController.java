package com.fleet.sso.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fleet.sso.feign.user.UserService;
import com.fleet.sso.json.R;

import javax.annotation.Resource;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @Resource
	UserService userService;

	@ResponseBody
	@RequestMapping("/get")
	public R login() {
		return R.ok("欢迎进入，您的身份是游客");
	}

}
