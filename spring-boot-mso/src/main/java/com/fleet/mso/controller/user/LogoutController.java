package com.fleet.mso.controller.user;

import com.fleet.mso.config.handler.BaseException;
import com.fleet.mso.controller.BaseController;
import com.fleet.mso.enums.ResultState;
import com.fleet.mso.json.R;
import com.fleet.mso.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping
public class LogoutController extends BaseController {

    @Resource
    RedisUtil redisUtil;

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public R logout(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = request.getParameter("accessToken");
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new BaseException(ResultState.ERROR, "缺少 accessToken");
        }

        Integer id = (Integer) redisUtil.get("accessToken:user:" + accessToken);
        String refreshToken = (String) redisUtil.get("accessToken:refreshToken:" + accessToken);
        List<String> refreshTokenList = (List<String>) redisUtil.get("user:refreshToken:" + id);
        if (refreshTokenList != null) {
            refreshTokenList.remove(refreshToken);
        }
        List<String> accessTokenList = (List<String>) redisUtil.get("user:accessToken:" + id);
        if (accessTokenList != null) {
            accessTokenList.remove(accessToken);
        }
        redisUtil.set("user:refreshToken:" + id, refreshTokenList);
        redisUtil.set("user:accessToken:" + id, accessTokenList);
        redisUtil.delete("refreshToken:user:" + refreshToken);
        redisUtil.delete("refreshToken:accessToken:" + refreshToken);
        redisUtil.delete("accessToken:user:" + accessToken);
        redisUtil.delete("accessToken:refreshToken:" + accessToken);

        return R.ok();
    }
}
