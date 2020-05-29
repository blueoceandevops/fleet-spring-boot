package com.fleet.sso.controller.user;

import com.fleet.common.annotation.Log;
import com.fleet.common.enums.ResultState;
import com.fleet.common.exception.BaseException;
import com.fleet.common.json.R;
import com.fleet.common.util.cache.RedisUtil;
import com.fleet.common.util.token.entity.Token;
import com.fleet.common.util.token.entity.UserToken;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Api(tags = "用户登出相关api")
@RestController
@RequestMapping
public class LogoutController {

    @Resource
    RedisUtil redisUtil;

    @Log(value = "登出", type = 3)
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public R logout(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = request.getParameter("accessToken");
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new BaseException(ResultState.ACCESS_TOKEN_MISSING);
        }

        Token userAccessToken = (Token) redisUtil.get("token:access:" + accessToken);
        if (userAccessToken != null) {
            Integer id = userAccessToken.getId();
            Set<String> keys = redisUtil.keys("token:user:" + id + ":*:access:" + accessToken);
            if (keys != null) {
                for (String key : keys) {
                    UserToken userToken = (UserToken) redisUtil.get(key);
                    if (userToken != null) {
                        redisUtil.delete("token:refresh:" + userToken.getRefreshToken());
                    }
                    redisUtil.delete(key);
                }
            }
            redisUtil.delete("token:access:" + accessToken);
        } else {
            return R.error(ResultState.ACCESS_TOKEN_INVALID);
        }
        return R.ok(ResultState.LOGOUT_SUCCESS);
    }
}
