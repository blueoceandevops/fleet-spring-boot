package com.fleet.sso.entity;

public class User {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 账户
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 加盐
     */
    private String pwdSalt;

    /**
     * 用户状态（0：禁用，1：正常，2：锁定）
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwdSalt() {
        return pwdSalt;
    }

    public void setPwdSalt(String pwdSalt) {
        this.pwdSalt = pwdSalt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
