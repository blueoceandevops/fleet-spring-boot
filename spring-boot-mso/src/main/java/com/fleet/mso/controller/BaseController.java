package com.fleet.mso.controller;

import com.fleet.mso.entity.User;
import com.fleet.mso.util.CurrentUser;

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
