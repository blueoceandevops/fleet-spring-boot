package com.fleet.sso.controller;

import com.fleet.sso.entity.User;
import com.fleet.sso.util.CurrentUser;

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
