package com.fleet.elasticsearch.service.impl;

import com.fleet.elasticsearch.dao.UserDao;
import com.fleet.elasticsearch.entity.User;
import com.fleet.elasticsearch.service.UserService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public Long insert(User user) {
        User userResult = userDao.save(user);
        return userResult.getId();
    }

    @Override
    public List<User> list(Integer pageNumber, Integer pageSize, String searchContent) {
        // 分页参数
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Function Score Query
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", searchContent)));

        // 创建搜索 DSL 查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(functionScoreQueryBuilder).build();
        Page<User> searchPageResults = userDao.search(searchQuery);
        return searchPageResults.getContent();
    }

}
