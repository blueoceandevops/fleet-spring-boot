package com.fleet.authcheck.entity;

import java.util.List;

public class Permit {

    /**
     * 权限项id
     */
    private Integer id;

    /**
     * 权限项代码
     */
    private String code;

    /**
     * 权限项备注
     */
    private String remark;

    /**
     * 上一级权限项id
     */
    private Integer upperId;

    private List<Permit> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUpperId() {
        return upperId;
    }

    public void setUpperId(Integer upperId) {
        this.upperId = upperId;
    }

    public List<Permit> getChildren() {
        return children;
    }

    public void setChildren(List<Permit> children) {
        this.children = children;
    }
}
