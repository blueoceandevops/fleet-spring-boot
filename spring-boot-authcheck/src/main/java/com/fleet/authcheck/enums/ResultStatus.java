package com.fleet.authcheck.enums;

public enum ResultStatus {

    /**
     * 200 成功
     **/
    SUCCESS(200, "成功"),
    /**
     * 400 错误
     **/
    ERROR(400, "错误");

    ResultStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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
}
