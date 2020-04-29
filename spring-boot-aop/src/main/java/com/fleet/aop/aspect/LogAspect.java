package com.fleet.aop.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

;

@Aspect
@Component
public class LogAspect {

    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(* com.fleet..*.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        logger.info("【请求URL】：{}", request.getRequestURL());
        logger.info("【请求IP】：{}", request.getRemoteAddr());
        logger.info("【请求方式】：{}", request.getMethod());
        logger.info("【请求方法名】：{}", signature.getDeclaringTypeName() + "." + signature.getName() + "()");

        // 参数名称
        String[] argNames = signature.getParameterNames();
        // 参数值
        Object[] argValues = pjp.getArgs();
        List<String> paramList = new ArrayList<>();
        if (argNames != null) {
            for (int i = 0; i < argNames.length; i++) {
                paramList.add(argNames[i] + ": " + argValues[i]);
            }
        }
        logger.info("【请求参数】：{}", "{" + StringUtils.join(paramList, ", ") + "}");
        return pjp.proceed();
    }

    @AfterThrowing(pointcut = "execution(* com.fleet..*.controller..*.*(..))", throwing = "e")
    public void doAfterThrowing(JoinPoint jp, Throwable e) {
        logger.error(e.getMessage());
    }
}
