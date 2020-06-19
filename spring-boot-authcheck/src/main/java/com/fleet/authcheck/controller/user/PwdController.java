package com.fleet.authcheck.controller.user;

import com.fleet.authcheck.config.handler.BaseException;
import com.fleet.authcheck.controller.BaseController;
import com.fleet.authcheck.entity.User;
import com.fleet.authcheck.enums.ResultStatus;
import com.fleet.authcheck.json.R;
import com.fleet.authcheck.service.UserService;
import com.fleet.authcheck.util.MD5Util;
import com.fleet.authcheck.util.RedisUtil;
import com.fleet.authcheck.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pwd")
public class PwdController extends BaseController {

    @Resource
    UserService userService;

    @Resource
    RedisUtil redisUtil;

    /**
     * 登陆
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    @RequestMapping("/change")
    public R login(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd) {
        if (StringUtils.isEmpty(oldPwd)) {
            throw new BaseException(ResultStatus.ERROR, "旧密码为空");
        }
        if (StringUtils.isEmpty(newPwd)) {
            throw new BaseException(ResultStatus.ERROR, "新密码为空");
        }

        // 当前用户
        User cur = getUser();
        if (cur == null) {
            throw new BaseException(ResultStatus.ERROR);
        }

        String pwd = MD5Util.encrypt(oldPwd, cur.getPwdSalt());
        if (!pwd.equals(cur.getPwd())) {
            throw new BaseException(ResultStatus.ERROR, "旧密码错误");
        }

        User user = new User();
        user.setId(cur.getId());
        // 生成新密码
        String salt = UUIDUtil.getUUID();
        user.setPwdSalt(salt);
        pwd = MD5Util.encrypt(newPwd, salt);
        user.setPwd(pwd);
        if (userService.update(user)) {
            return R.ok();
        }
        return R.error();
    }
}
