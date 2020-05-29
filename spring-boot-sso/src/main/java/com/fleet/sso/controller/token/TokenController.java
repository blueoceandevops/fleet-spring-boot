package com.fleet.sso.controller.token;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fleet.sso.config.property.CustomSetting;
import com.fleet.sso.util.RedisUtil;
import com.fleet.sso.feign.user.UserService;
import com.fleet.sso.enums.ResultStatus;
import com.fleet.sso.enums.TokenExpiresIn;
import com.fleet.sso.json.R;
import com.fleet.util.UUIDUtil;
import com.fleet.util.token.TokenUtil;
import com.fleet.util.token.entity.UserAccessToken;
import com.fleet.util.token.entity.UserRefreshToken;
import com.fleet.util.token.entity.UserToken;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/token")
public class TokenController {

	@Resource
	UserService userService;

    @Resource
	RedisUtil redisUtil;

	@Resource
	CustomSetting customSetting;

	/**
	 * 刷新token
	 *
	 * @param refreshToken
	 * @return
	 */
	@RequestMapping(value = "/refresh")
	public R login(@RequestParam("refreshToken") String refreshToken) {
		UserRefreshToken userRefreshToken = (UserRefreshToken) redisUtil.get("refreshToken:" + refreshToken);
		if (userRefreshToken == null) {
			return R.error(ResultStatus.NOT_LOGIN_PROMPT);
		}
		if (TokenUtil.checkUserRefreshTokenForExpiration(userRefreshToken, TokenExpiresIn.REFRESH_EXPIRES_IN.getMsec())) {
			return R.error(ResultStatus.REFRESH_TOKEN_EXPIRE, "需要重新登陆");
		}
		Integer id = userRefreshToken.getId();
		UserToken userToken = (UserToken) redisUtil.get("userToken:" + id);
		if (userToken == null) {
			return R.error(ResultStatus.NOT_LOGIN_PROMPT);
		}
		redisUtil.delete("accessToken:" + userToken.getAccessToken());

		Long issuedAt = System.currentTimeMillis();
		UserAccessToken userAccessToken = new UserAccessToken();
		userAccessToken.setId(id);
		String accessToken = UUIDUtil.getUUID();
		userAccessToken.setAccessToken(accessToken);
		userAccessToken.setExpiresIn(TokenExpiresIn.EXPIRES_IN.getMsec());
		userAccessToken.setIssuedAt(issuedAt);
		redisUtil.set("accessToken:" + accessToken, userAccessToken);

		// 更新userToken中关于accessToken的值
		userToken.setAccessToken(accessToken);
		redisUtil.set("userToken:" + id, userToken);

		Map<String, Object> map = new HashMap<>();
		map.put("accessToken", accessToken);
		map.put("expiresIn", TokenExpiresIn.EXPIRES_IN.getMsec());
		map.put("issuedAt", issuedAt);
		return R.ok(map);
	}

}
