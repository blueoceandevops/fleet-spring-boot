package com.fleet.mso.config.handler;

import com.fleet.mso.enums.ResultState;

import java.io.Serializable;

public class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 4253696777296748794L;

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseException() {
        super("失败");
        this.code = 400;
        this.msg = "失败";
    }

    public BaseException(String msg) {
        super(msg);
        this.code = 400;
        this.msg = msg;
    }

    public BaseException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(ResultState resultState) {
        super(resultState.getMsg());
        this.code = resultState.getCode();
        this.msg = resultState.getMsg();
    }

    public BaseException(ResultState resultState, String msg) {
        super(msg);
        this.code = resultState.getCode();
        this.msg = msg;
    }
}
