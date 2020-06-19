package com.fleet.authcheck.controller.user;

import com.fleet.authcheck.config.aspect.annotation.AuthCheck;
import com.fleet.authcheck.json.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @AuthCheck(permits = "admin:get")
	@RequestMapping(value = "/get")
	public R getMessage() {
		return R.ok("您拥有管理员权限，可以获得该接口的信息！");
	}
}
