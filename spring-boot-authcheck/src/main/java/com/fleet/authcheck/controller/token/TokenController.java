package com.fleet.authcheck.controller.token;

import com.fleet.authcheck.config.handler.BaseException;
import com.fleet.authcheck.controller.BaseController;
import com.fleet.authcheck.enums.ResultState;
import com.fleet.authcheck.enums.TokenExpiresIn;
import com.fleet.authcheck.json.R;
import com.fleet.authcheck.util.RedisUtil;
import com.fleet.authcheck.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/token")
public class TokenController extends BaseController {

    @Resource
    RedisUtil redisUtil;

    /**
     * 刷新 accessToken
     */
    @RequestMapping(value = "/refresh", method = {RequestMethod.GET, RequestMethod.POST})
    public R refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");
        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = request.getParameter("refreshToken");
        }
        if (StringUtils.isEmpty(refreshToken)) {
            throw new BaseException(ResultState.ERROR, "缺少 refreshToken");
        }

        Integer id = (Integer) redisUtil.get("refreshToken:user:" + refreshToken);
        if (id == null) {
            throw new BaseException("refreshToken 无效或已过期");
        }

        String accessToken = (String) redisUtil.get("refreshToken:accessToken:" + refreshToken);
        if (accessToken != null) {
            redisUtil.delete("accessToken:user:" + accessToken);
        }
        redisUtil.delete("refreshToken:accessToken:" + refreshToken);

        accessToken = UUIDUtil.getUUID();
        redisUtil.setEx("refreshToken:accessToken:" + refreshToken, accessToken, TokenExpiresIn.ACCESS_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        redisUtil.setEx("accessToken:user:" + accessToken, id, TokenExpiresIn.ACCESS_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        return R.ok(accessToken);
    }
}
