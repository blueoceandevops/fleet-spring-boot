package com.fleet.sso.controller;

import com.fleet.entity.app.App;
import com.fleet.json.R;
import com.fleet.user.feign.AppFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

	@Autowired
	AppFeignClient appFeignClient;

	@RequestMapping(path = "")
	public R get(String appId, String appSecret) {
		App app = new App();
		app.setAppId(appId);
		app.setAppSecret(appSecret);
		R r = appFeignClient.check(app);
		if (!R.isOk(r)) {
			return r;
		}

		return R.ok();
	}

	// @RequestMapping(path = "/accessToken")
	// public Token accessToken(String name, String userPassword) {
	// return TokenUtilAAAA.geToken(name, userPassword,
	// TokenExpiresIn.EXPIRES_IN);
	// }
	//
	// @RequestMapping(path = "/refreshToken")
	// public Token refreshToken(String name, String userPassword) {
	// return TokenUtilAAAA.geToken(name, userPassword,
	// TokenExpiresIn.REFRESH_EXPIRES_IN);
	// }
	//
	// @RequestMapping(path = "/verify")
	// public Boolean verify(String token) {
	// return TokenUtilAAAA.verify(token);
	// }

}
