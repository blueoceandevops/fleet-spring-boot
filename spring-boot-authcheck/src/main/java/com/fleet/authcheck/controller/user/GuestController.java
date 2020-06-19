package com.fleet.authcheck.controller.user;

import com.fleet.authcheck.json.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @RequestMapping("/get")
    public R login() {
        return R.ok("欢迎进入，您的身份是游客");
    }
}
