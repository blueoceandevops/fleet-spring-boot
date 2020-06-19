package com.fleet.authcheck.config.aspect;

import com.fleet.authcheck.config.aspect.annotation.AuthCheck;
import com.fleet.authcheck.config.handler.BaseException;
import com.fleet.authcheck.entity.User;
import com.fleet.authcheck.service.UserService;
import com.fleet.authcheck.util.CurrentUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
public class AuthCheckAspect {

    @Resource
    private UserService userService;

    @Around("@annotation(com.fleet.authcheck.config.aspect.annotation.AuthCheck)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        AuthCheck authCheck = method.getAnnotation(AuthCheck.class);

        User user = CurrentUser.getUser();
        if (user == null) {
            throw new BaseException();
        }
        Integer userId = user.getId();
        String[] roles = authCheck.roles();
        if (roles.length != 0) {
            Boolean hasRoles = userService.hasRoles(userId, roles);
            if (!hasRoles) {
                throw new BaseException("角色未授权");
            }
        }

        String[] permits = authCheck.permits();
        if (permits.length != 0) {
            Boolean hasPermits = userService.hasPermits(userId, permits);
            if (!hasPermits) {
                throw new BaseException("权限项未授权");
            }
        }
        return pjp.proceed();
    }
}
