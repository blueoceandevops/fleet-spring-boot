package com.fleet.security.config.handler;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationFailure implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=utf-8");
        String r = null;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            r = "账户名或者密码输入错误";
        } else if (exception instanceof LockedException) {
            r = "账户被锁定";
        } else if (exception instanceof CredentialsExpiredException) {
            r = "密码过期";
        } else if (exception instanceof AccountExpiredException) {
            r = "账户过期";
        } else if (exception instanceof DisabledException) {
            r = "账户被禁用";
        } else {
            r = "登录失败";
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.write(r);
        printWriter.flush();
        printWriter.close();
    }
}
