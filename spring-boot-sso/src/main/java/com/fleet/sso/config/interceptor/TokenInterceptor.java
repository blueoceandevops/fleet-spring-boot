package com.fleet.sso.config.interceptor;

import com.fleet.sso.config.handler.BaseException;
import com.fleet.sso.entity.User;
import com.fleet.sso.enums.ResultState;
import com.fleet.sso.service.UserService;
import com.fleet.sso.util.CurrentUser;
import com.fleet.sso.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    RedisUtil redisUtil;

    @Resource
    private UserService userService;

    /**
     * 设置 refreshToken 验证，refreshToken 验证后不再验证 accessToken
     */
    private List<String> refreshTokenPatterns = new ArrayList<>();

    public TokenInterceptor() {
    }

    public TokenInterceptor(List<String> patternList) {
        this.refreshTokenPatterns.addAll(patternList);
    }

    public void setRefreshTokenPatterns(List<String> patternList) {
        this.refreshTokenPatterns.addAll(patternList);
    }

    public void setRefreshTokenPatterns(String... patterns) {
        this.refreshTokenPatterns.addAll(Arrays.asList(patterns));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (this.refreshTokenPatterns.size() != 0) {
            if (this.refreshTokenPatterns.contains("/**") || this.refreshTokenPatterns.contains(uri)) {
                String refreshToken = request.getHeader("refreshToken");
                if (StringUtils.isEmpty(refreshToken)) {
                    refreshToken = request.getParameter("refreshToken");
                }
                if (StringUtils.isEmpty(refreshToken)) {
                    throw new BaseException(ResultState.ERROR, "缺少 refreshToken");
                }

                Integer id = (Integer) redisUtil.get("refreshToken:user:" + refreshToken);
                if (id != null) {
                    if (CurrentUser.getUser() == null) {
                        User user = new User();
                        user.setId(id);
                        user = userService.get(user);
                        CurrentUser.setUser(user);
                    }
                    return true;
                } else {
                    throw new BaseException("refreshToken 无效或已过期");
                }
            }
        }

        String accessToken = request.getHeader("accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            accessToken = request.getParameter("accessToken");
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new BaseException(ResultState.ERROR, "缺少 accessToken");
        }

        Integer id = (Integer) redisUtil.get("accessToken:user:" + accessToken);
        if (id != null) {
            if (CurrentUser.getUser() == null) {
                User user = new User();
                user.setId(id);
                user = userService.get(user);
                CurrentUser.setUser(user);
            }
            return true;
        } else {
            throw new BaseException("accessToken 无效或已过期");
        }
    }
}
