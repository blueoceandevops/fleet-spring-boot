# spring-boot-thymeleaf

> Spring Boot 结合 Thymeleaf 模板引擎

## 环境搭建

> 配置 IDEA 修改 Thymeleaf 的 html 模板不需要重启应用

1. Edit Configurations -> configuration 按图示配置

/step/step1.1.png
/step/step1.2.png

2. Help -> Find action 输入 `Registry` 按图示配置

/step/step2.1.png
/step/step1.2.png

3. File -> Settings -> Build,Execution,Deployment -> Compiler 按图示配置

/step/step3.png

4. 在 application.yml 中配置  `spring.thymeleaf.cache=false`
