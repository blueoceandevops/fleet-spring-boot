package com.fleet.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromptController {

    /**
     * 未登录提示
     */
    @RequestMapping(value = "/notIn")
    public String notIn() {
        return "未登录";
    }

    /**
     * 已登录提示
     */
    @RequestMapping(value = "/in")
    public String in() {
        return "已登录";
    }

    /**
     * 登出失败提示
     *
     * @return
     */
    @RequestMapping(value = "/inFailed")
    public String inFailed() {
        return "登录失败";
    }

    /**
     * 已登出提示
     *
     * @return
     */
    @RequestMapping(value = "/out")
    public String out() {
        return "已登出";
    }

    /**
     * 登出失败提示
     *
     * @return
     */
    @RequestMapping(value = "/outFailed")
    public String outFailed() {
        return "登出失败";
    }

    /**
     * 已登出提示
     *
     * @return
     */
    @RequestMapping(value = "/unauth")
    public String unauth() {
        return "无权限";
    }
}
