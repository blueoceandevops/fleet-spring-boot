package com.fleet.sso.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleet.sso.json.R;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping(value = "/get")
	public R getMessage() {
		return R.ok("您拥有管理员权限，可以获得该接口的信息！");
	}
}
