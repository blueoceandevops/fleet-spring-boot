package com.fleet.mso.controller.token;

import com.fleet.mso.config.handler.BaseException;
import com.fleet.mso.controller.BaseController;
import com.fleet.mso.enums.ResultState;
import com.fleet.mso.enums.TokenExpiresIn;
import com.fleet.mso.json.R;
import com.fleet.mso.util.RedisUtil;
import com.fleet.mso.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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
        String accessToken = (String) redisUtil.get("refreshToken:accessToken:" + refreshToken);
        if (accessToken != null) {
            redisUtil.delete("accessToken:user:" + accessToken);
            redisUtil.delete("accessToken:refreshToken:" + accessToken);
        }

        List<String> accessTokenList = (List<String>) redisUtil.get("user:accessToken:" + id);
        if (accessTokenList != null) {
            if (accessToken != null) {
                accessTokenList.remove(accessToken);
            }
        } else {
            accessTokenList = new ArrayList<>();
        }
        accessToken = UUIDUtil.getUUID();
        accessTokenList.add(accessToken);
        redisUtil.set("user:accessToken:" + id, accessTokenList);
        redisUtil.setEx("refreshToken:accessToken:" + refreshToken, accessToken, redisUtil.getExpire("refreshToken:accessToken:" + refreshToken, TimeUnit.SECONDS), TimeUnit.SECONDS);
        redisUtil.setEx("accessToken:user:" + accessToken, id, TokenExpiresIn.ACCESS_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        redisUtil.setEx("accessToken:refreshToken:" + accessToken, refreshToken, TokenExpiresIn.ACCESS_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        return R.ok(accessToken);
    }
}
