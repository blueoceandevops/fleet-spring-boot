package com.fleet.jsoup.controller;

import com.fleet.jsoup.entity.Article;
import com.fleet.jsoup.entity.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @RequestMapping("/insert")
    public String insert(@RequestBody Article article) {
        System.out.println(article.getContent());
        return "成功";
    }
}
