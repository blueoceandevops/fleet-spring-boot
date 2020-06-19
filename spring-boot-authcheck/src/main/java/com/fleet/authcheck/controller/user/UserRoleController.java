package com.fleet.authcheck.controller.user;

import com.fleet.authcheck.entity.UserRole;
import com.fleet.authcheck.page.PageUtil;
import com.fleet.authcheck.page.entity.Page;
import com.fleet.authcheck.service.UserRoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/role")
public class UserRoleController {

    @Resource
    private UserRoleService userRoleService;

    @RequestMapping("/insert")
    public Boolean insert(@RequestBody UserRole userRole) {
        return userRoleService.insert(userRole);
    }

    @RequestMapping("/delete")
    public Boolean delete(@RequestBody UserRole userRole) {
        return userRoleService.delete(userRole);
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody UserRole userRole) {
        return userRoleService.update(userRole);
    }

    @RequestMapping("/get")
    public UserRole get(@RequestBody UserRole userRole) {
        return userRoleService.get(userRole);
    }

    @RequestMapping("/list")
    public List<UserRole> list(@RequestParam Map<String, Object> map) {
        return userRoleService.list(map);
    }

    @RequestMapping("/listPage")
    public PageUtil<UserRole> listPage(@RequestBody Page page) {
        return userRoleService.listPage(page);
    }
}
