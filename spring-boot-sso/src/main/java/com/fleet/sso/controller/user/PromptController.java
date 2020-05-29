package com.fleet.sso.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleet.sso.enums.ResultStatus;
import com.fleet.sso.json.R;

@RestController
@RequestMapping("/prompt")
public class PromptController {

	/**
	 * 已登录提示
	 *
	 * @return
	 */
	@RequestMapping(value = "/loggedIn")
	public R loggedIn() {
		return R.error(ResultStatus.LOGGED_IN_PROMPT);
	}

	/**
	 * 已登出提示
	 *
	 * @return
	 */
	@RequestMapping(value = "/loggedOut")
	public R loggedOut() {
		return R.error(ResultStatus.LOGGED_OUT_PROMPT);
	}

}
