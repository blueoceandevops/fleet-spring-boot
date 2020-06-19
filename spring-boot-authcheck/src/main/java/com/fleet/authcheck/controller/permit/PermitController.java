package com.fleet.authcheck.controller.permit;

import com.fleet.authcheck.entity.Permit;
import com.fleet.authcheck.page.PageUtil;
import com.fleet.authcheck.page.entity.Page;
import com.fleet.authcheck.service.PermitService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permit")
public class PermitController {

    @Resource
    private PermitService permitService;

    @RequestMapping("/insert")
    public Boolean insert(@RequestBody Permit permit) {
        return permitService.insert(permit);
    }

    @RequestMapping("/delete")
    public Boolean delete(@RequestBody Permit permit) {
        return permitService.delete(permit);
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody Permit permit) {
        return permitService.update(permit);
    }

    @RequestMapping("/get")
    public Permit get(@RequestBody Permit permit) {
        return permitService.get(permit);
    }

    @RequestMapping("/list")
    public List<Permit> list(@RequestParam Map<String, Object> map) {
        return permitService.list(map);
    }

    @RequestMapping("/listPage")
    public PageUtil<Permit> listPage(@RequestBody Page page) {
        return permitService.listPage(page);
    }
}
