package com.fleet.mybatis.plus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fleet.mybatis.plus.entity.User;
import com.fleet.mybatis.plus.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void listPage() {
        Page<User> page = new Page<>(2, 1);
        Map<String, Object> map = new HashMap<>();
        Page<User> listPage = userService.listPage(page, map);
        System.out.println(listPage);
    }
}
