package com.fleet.authcheck.controller.role;

import com.fleet.authcheck.entity.Role;
import com.fleet.authcheck.page.PageUtil;
import com.fleet.authcheck.page.entity.Page;
import com.fleet.authcheck.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping("/insert")
    public Boolean insert(@RequestBody Role role) {
        return roleService.insert(role);
    }

    @RequestMapping("/delete")
    public Boolean delete(@RequestBody Role role) {
        return roleService.delete(role);
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody Role role) {
        return roleService.update(role);
    }

    @RequestMapping("/get")
    public Role get(@RequestBody Role role) {
        return roleService.get(role);
    }

    @RequestMapping("/list")
    public List<Role> list(@RequestParam Map<String, Object> map) {
        return roleService.list(map);
    }

    @RequestMapping("/listPage")
    public PageUtil<Role> listPage(@RequestBody Page page) {
        return roleService.listPage(page);
    }
}
