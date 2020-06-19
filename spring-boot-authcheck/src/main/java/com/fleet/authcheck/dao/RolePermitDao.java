package com.fleet.authcheck.dao;

import com.fleet.authcheck.entity.RolePermit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RolePermitDao extends BaseDao<RolePermit> {
}
