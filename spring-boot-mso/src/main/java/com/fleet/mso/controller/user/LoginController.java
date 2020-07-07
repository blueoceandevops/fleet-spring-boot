package com.fleet.mso.controller.user;

import com.fleet.mso.entity.User;
import com.fleet.mso.enums.ResultState;
import com.fleet.mso.enums.TokenExpiresIn;
import com.fleet.mso.json.R;
import com.fleet.mso.service.UserService;
import com.fleet.mso.util.MD5Util;
import com.fleet.mso.util.RedisUtil;
import com.fleet.mso.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    UserService userService;

    @Resource
    RedisUtil redisUtil;

    /**
     * 登陆
     *
     * @param name 账户
     * @param pwd  密码
     */
    @GetMapping("/login")
    public R login(@RequestParam("name") String name, @RequestParam("pwd") String pwd) {
        if (StringUtils.isEmpty(name)) {
            return R.error(ResultState.ERROR, "账户为空");
        }
        if (StringUtils.isEmpty(pwd)) {
            return R.error(ResultState.ERROR, "密码为空");
        }

        User user = new User();
        user.setName(name);
        user = userService.get(user);
        if (user == null) {
            return R.error(ResultState.ERROR, "账户或密码错误");
        }

        pwd = MD5Util.encrypt(pwd, user.getPwdSalt());
        if (!pwd.equals(user.getPwd())) {
            return R.error(ResultState.ERROR, "账户或密码错误");
        }

        if (user.getState().equals(0)) {
            return R.error(ResultState.ERROR, "账户被禁用");
        }
        if (user.getState().equals(2)) {
            return R.error(ResultState.ERROR, "账户被锁定");
        }

        Integer id = user.getId();
        Map<String, Object> tokenMap = initToken(id);
        return R.ok(tokenMap);
    }

    public Map<String, Object> initToken(Integer id) {
        Map<String, Object> tokenMap = new HashMap<>();
        String refreshToken = UUIDUtil.getUUID();
        String accessToken = UUIDUtil.getUUID();
        List<String> refreshTokenList = (List<String>) redisUtil.get("user:refreshToken:" + id);
        if (refreshTokenList == null) {
            refreshTokenList = new ArrayList<>();
        }
        List<String> accessTokenList = (List<String>) redisUtil.get("user:accessToken:" + id);
        if (accessTokenList == null) {
            accessTokenList = new ArrayList<>();
        }
        refreshTokenList.add(refreshToken);
        accessTokenList.add(accessToken);
        redisUtil.set("user:refreshToken:" + id, refreshTokenList);
        redisUtil.set("user:accessToken:" + id, accessTokenList);
        redisUtil.setEx("refreshToken:user:" + refreshToken, id, TokenExpiresIn.REFRESH_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        redisUtil.setEx("refreshToken:accessToken:" + refreshToken, accessToken, TokenExpiresIn.REFRESH_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        redisUtil.setEx("accessToken:user:" + accessToken, id, TokenExpiresIn.ACCESS_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        redisUtil.setEx("accessToken:refreshToken:" + accessToken, refreshToken, TokenExpiresIn.ACCESS_EXPIRES_IN.getSec(), TimeUnit.SECONDS);
        tokenMap.put("refreshToken", refreshToken);
        tokenMap.put("accessToken", accessToken);
        return tokenMap;
    }
}
