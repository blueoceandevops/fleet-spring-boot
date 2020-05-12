package com.fleet.mysql.multi.aop.config.aspect;

import com.fleet.mysql.multi.aop.config.DataSourceConfig;
import com.fleet.mysql.multi.aop.enums.DBType;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAspect {

    @Before("!@annotation(com.fleet.mysql.multi.aop.annotation.Master) "
            + "&& (execution(* com.fleet.mysql.proxy.service..*.select*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.get*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.list*(..)))")
    public void read() {
        DataSourceConfig.DataSourceType.setDBType(DBType.SLAVE);
    }

    @Before("@annotation(com.fleet.mysql.multi.aop.annotation.Master) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.insert*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.add*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.update*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.edit*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.delete*(..)) "
            + "|| execution(* com.fleet.mysql.proxy.service..*.remove*(..))")
    public void write() {
        DataSourceConfig.DataSourceType.setDBType(DBType.MASTER);
    }
}
