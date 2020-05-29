package com.fleet.sso.config.interceptor;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fleet.sso.config.handler.BaseException;
import com.fleet.sso.config.property.CustomSetting;
import com.fleet.sso.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONArray;
import com.fleet.util.AESUtil;
import com.fleet.sso.feign.base.app.AppFeignClient;
import com.fleet.entity.app.App;
import com.fleet.sso.enums.ResultStatus;
import com.fleet.exception.BaseException;
import com.fleet.util.token.entity.UserAccessToken;

public class TokenAuthInterceptor implements HandlerInterceptor {

    @Resource
    RedisUtil redisUtil;

    @Resource
	AppFeignClient appFeignClient;

    @Resource
    CustomSetting customSetting;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String appId = request.getHeader("appId");
		if (appId == null) {
			throw new BaseException(ResultStatus.ERROR);
		}
		String accessToken = request.getHeader("accessToken");
		if (accessToken == null) {
			throw new BaseException(ResultStatus.ERROR);
		}
		String timestampStr = request.getHeader("timestamp");
		if (timestampStr == null) {
			throw new BaseException(ResultStatus.ERROR);
		}
		String sign = request.getHeader("sign");
		if (sign == null) {
			throw new BaseException(ResultStatus.ERROR);
		}

		Long timestamp = Long.parseLong(timestampStr);
		if ((System.currentTimeMillis() - timestamp) > customSetting.getTimestampExpireTime() * 60000) {
			throw new BaseException(ResultStatus.ERROR);
		}

		String decrypt = AESUtil.decrypt(sign);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSONArray.parseObject(decrypt, Map.class);
		for (String key : map.keySet()) {
			String signValue = map.get(key).toString();
			String requestValue = request.getHeader(key);
			if (requestValue == null) {
				/**
				 * get方式传参，校验参数，post传参，不校验参数
				 */
				if (request.getMethod().equals("GET")) {
					requestValue = request.getParameter(key);
				} else if (request.getMethod().equals("POST")) {
					continue;
				}
			}
			if (requestValue == null) {
				throw new BaseException(ResultStatus.PARAM_MISSING, key + "参数缺少");
			}
			if (!signValue.equals(requestValue)) {
				throw new BaseException(ResultStatus.PARAM_TAMPERED, key + "参数被篡改");
			}
		}

		App app = new App();
		app.setAppId(appId);
		app = appFeignClient.get(app);
		if (app == null) {
			throw new BaseException(ResultStatus.APP_ID_INVALID);
		} else {
			if (app.getIsDeleted() == 1) {
				throw new BaseException(ResultStatus.APP_ID_GONE);
			}
		}

		UserAccessToken userAccessToken = (UserAccessToken) redisUtil.get("accessToken:" + accessToken);
		if ((System.currentTimeMillis() - userAccessToken.getIssuedAt()) > userAccessToken.getExpiresIn() * 1000) {
			throw new BaseException(ResultStatus.ACCESS_TOKEN_EXPIRE);
		}

		return true;
	}

}
