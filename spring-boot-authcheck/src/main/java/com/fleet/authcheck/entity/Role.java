package com.fleet.authcheck.entity;

import java.util.List;

public class Role {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色备注
     */
    private String remark;

    /**
     * 上一级角色id
     */
    private Integer upperId;

    private List<Role> children;

    private List<RolePermit> rolePermitList;

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

    public List<Role> getChildren() {
        return children;
    }

    public void setChildren(List<Role> children) {
        this.children = children;
    }

    public List<RolePermit> getRolePermitList() {
        return rolePermitList;
    }

    public void setRolePermitList(List<RolePermit> rolePermitList) {
        this.rolePermitList = rolePermitList;
    }
}
