package com.fleet.submit.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    public String handleBaseException(BaseException e) {
        return e.getMsg();
    }

    /**
     * 全局异常捕捉处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e) {
        return "失败";
    }
}
