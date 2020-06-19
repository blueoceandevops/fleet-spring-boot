package com.fleet.authcheck.controller;

import com.fleet.authcheck.entity.User;
import com.fleet.authcheck.util.CurrentUser;

public class BaseController {

    public User getUser() {
        return CurrentUser.getUser();
    }

    public Integer getId() {
        User user = getUser();
        if (user == null) {
            return null;
        }
        return getUser().getId();
    }
}
