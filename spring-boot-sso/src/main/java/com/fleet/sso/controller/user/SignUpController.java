package com.fleet.sso.controller.user;

import com.fleet.sso.entity.User;
import com.fleet.sso.json.R;
import com.fleet.sso.service.UserService;
import com.fleet.sso.util.MD5Util;
import com.fleet.sso.util.UUIDUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SignUpController {

    @Resource
    UserService userService;

    @RequestMapping("signUp")
    public R insert(@RequestBody User user) {
        User u = new User();
        u.setName(user.getName());
        u = userService.get(u);
        if (u != null) {
            return R.error("账户（" + user.getName() + "）已存在");
        }

        String pwdSalt = UUIDUtil.getUUID();
        user.setPwdSalt(pwdSalt);
        String pwd = MD5Util.encrypt(user.getPwd(), pwdSalt);
        user.setPwd(pwd);

        if (userService.insert(user)) {
            return R.ok();
        }
        return R.error();
    }
}
