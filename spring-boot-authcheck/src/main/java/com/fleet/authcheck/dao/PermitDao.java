package com.fleet.authcheck.dao;

import com.fleet.authcheck.entity.Permit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PermitDao extends BaseDao<Permit> {
}
