package com.fleet.submit.handler;

import java.io.Serializable;

public class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 4253696777296748794L;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseException() {
        super("失败");
        this.msg = "失败";
    }

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
