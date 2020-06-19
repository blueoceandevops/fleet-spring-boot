package com.fleet.authcheck.controller.role;

import com.fleet.authcheck.entity.RolePermit;
import com.fleet.authcheck.page.PageUtil;
import com.fleet.authcheck.page.entity.Page;
import com.fleet.authcheck.service.RolePermitService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role/permit")
public class RolePermitController {

    @Resource
    private RolePermitService rolePermitService;

    @RequestMapping("/insert")
    public Boolean insert(@RequestBody RolePermit rolePermit) {
        return rolePermitService.insert(rolePermit);
    }

    @RequestMapping("/delete")
    public Boolean delete(@RequestBody RolePermit rolePermit) {
        return rolePermitService.delete(rolePermit);
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody RolePermit rolePermit) {
        return rolePermitService.update(rolePermit);
    }

    @RequestMapping("/get")
    public RolePermit get(@RequestBody RolePermit rolePermit) {
        return rolePermitService.get(rolePermit);
    }

    @RequestMapping("/list")
    public List<RolePermit> list(@RequestParam Map<String, Object> map) {
        return rolePermitService.list(map);
    }

    @RequestMapping("/listPage")
    public PageUtil<RolePermit> listPage(@RequestBody Page page) {
        return rolePermitService.listPage(page);
    }
}
