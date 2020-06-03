package com.fleet.sso.config.interceptor;

import com.fleet.sso.entity.User;
import com.fleet.sso.service.UserService;
import com.fleet.sso.util.CurrentUser;
import com.fleet.sso.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String refreshToken = request.getHeader("refreshToken");
        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = request.getParameter("refreshToken");
        }
        if (StringUtils.isNotEmpty(refreshToken)) {
            Integer id = (Integer) redisUtil.get("refreshToken:user:" + refreshToken);
            if (id != null) {
                if (CurrentUser.getUser() == null) {
                    User user = new User();
                    user.setId(id);
                    user = userService.get(user);
                    CurrentUser.setUser(user);
                }
            }
        } else {
            String accessToken = request.getHeader("accessToken");
            if (StringUtils.isEmpty(accessToken)) {
                accessToken = request.getParameter("accessToken");
            }
            if (StringUtils.isNotEmpty(accessToken)) {
                Integer id = (Integer) redisUtil.get("accessToken:user:" + accessToken);
                if (id != null) {
                    if (CurrentUser.getUser() == null) {
                        User user = new User();
                        user.setId(id);
                        user = userService.get(user);
                        CurrentUser.setUser(user);
                    }
                }
            }
        }
        return true;
    }
}
