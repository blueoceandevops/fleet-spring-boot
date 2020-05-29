package com.fleet.sso.controller.user;

import com.fleet.common.enums.TokenExpiresIn;
import com.fleet.common.util.UUIDUtil;
import com.fleet.common.util.token.entity.Token;
import com.fleet.common.util.token.entity.UserToken;
import com.fleet.sso.entity.User;
import com.fleet.sso.enums.ResultStatus;
import com.fleet.sso.json.R;
import com.fleet.sso.service.UserService;
import com.fleet.sso.util.MD5Util;
import com.fleet.sso.util.RedisUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

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
            return R.error(ResultStatus.ERROR, "账户为空");
        }
        if (StringUtils.isEmpty(pwd)) {
            return R.error(ResultStatus.ERROR, "密码为空");
        }

        User user = new User();
        user.setName(name);
        user = userService.get(user);
        if (user == null) {
            return R.error(ResultStatus.ERROR, "账户或密码错误");
        }

        pwd = MD5Util.encrypt(pwd, user.getPwdSalt());
        if (!pwd.equals(user.getPwd())) {
            return R.error(ResultStatus.ERROR, "账户或密码错误");
        }

        if (user.getStatus().equals(0)) {
            return R.error(ResultStatus.ERROR, "账户被禁用");
        }
        if (user.getStatus().equals(2)) {
            return R.error(ResultStatus.ERROR, "账户被锁定");
        }

        Integer id = user.getId();
        Integer single = user.getSingle();
        if (single.equals(1)) {
            Set<String> keys = redisUtil.keys("token:user:" + id + ":*");
            if (keys != null) {
                for (String key : keys) {
                    UserToken userToken = (UserToken) redisUtil.get(key);
                    if (userToken != null) {
                        redisUtil.delete("token:access:" + userToken.getAccessToken());
                        redisUtil.delete("token:refresh:" + userToken.getRefreshToken());
                    }
                    redisUtil.delete(key);
                }
            }
        }

        UserToken userToken = setUserToken(id);
        return R.ok(userToken);
    }

    public UserToken setUserToken(Integer id) {
        Long issuedAt = System.currentTimeMillis();
        String accessToken = UUIDUtil.getUUID();
        String refreshToken = UUIDUtil.getUUID();

        // 设置用户 accessToken 信息
        Token userAccessToken = new Token();
        userAccessToken.setId(id);
        userAccessToken.setToken(accessToken);
        userAccessToken.setIssuedAt(issuedAt);
        userAccessToken.setExpiresIn(TokenExpiresIn.EXPIRES_IN.getMsec());
        redisUtil.set("token:access:" + accessToken, userAccessToken);

        // 设置用户 refreshToken 信息
        Token userRefreshToken = new Token();
        userRefreshToken.setId(id);
        userRefreshToken.setToken(refreshToken);
        userRefreshToken.setIssuedAt(issuedAt);
        userRefreshToken.setExpiresIn(TokenExpiresIn.REFRESH_EXPIRES_IN.getMsec());
        redisUtil.set("token:refresh:" + refreshToken, userRefreshToken);

        // 设置用户 token 关联信息
        UserToken userToken = new UserToken();
        userToken.setAccessToken(accessToken);
        userToken.setRefreshToken(refreshToken);
        redisUtil.set("token:user:" + id + ":refresh:" + refreshToken + ":access:" + accessToken, userToken);

        return userToken;
    }
}
