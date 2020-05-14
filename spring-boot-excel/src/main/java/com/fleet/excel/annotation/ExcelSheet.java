package com.fleet.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExcelSheet {

    /**
     * 模板文件
     */
    String template() default "";

    /**
     * 读取哪一个sheet中
     */
    int sheetAt() default 0;

    /**
     * 表头所在行
     */
    int headAt() default 0;

    /**
     * 从哪一行开始读写数据(值为行的下标)
     */
    int startWith() default 1;

}
