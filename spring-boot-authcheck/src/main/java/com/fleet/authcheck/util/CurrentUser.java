package com.fleet.authcheck.util;

import com.fleet.authcheck.entity.User;

public class CurrentUser {

    private static final ThreadLocal<User> context = new ThreadLocal<>();

    public static void remove() {
        CurrentUser.context.remove();
    }

    public static void setUser(User user) {
        CurrentUser.remove();
        context.set(user);
    }

    public static User getUser() {
        return context.get();
    }
}
