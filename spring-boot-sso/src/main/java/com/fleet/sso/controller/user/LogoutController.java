package com.fleet.sso.controller.user;

import com.fleet.sso.config.handler.BaseException;
import com.fleet.sso.controller.BaseController;
import com.fleet.sso.enums.ResultStatus;
import com.fleet.sso.json.R;
import com.fleet.sso.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
            throw new BaseException(ResultStatus.ERROR, "缺少 accessToken");
        }

        Integer id = getId();
        if (id == null) {
            return R.error("当前用户不存在");
        }
        clearToken(id);
        return R.ok();
    }

    public void clearToken(Integer id) {
        String refreshToken = (String) redisUtil.get("user:refreshToken:" + id);
        if (StringUtils.isNotEmpty(refreshToken)) {
            redisUtil.delete("refreshToken:user:" + refreshToken);
            String accessToken = (String) redisUtil.get("refreshToken:accessToken:" + refreshToken);
            if (StringUtils.isNotEmpty(accessToken)) {
                redisUtil.delete("accessToken:user:" + accessToken);
            }
            redisUtil.delete("refreshToken:accessToken:" + refreshToken);
        }
        redisUtil.delete("user:refreshToken:" + id);
    }
}
