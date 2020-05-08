package com.fleet.hystrix.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixConfig {

    @Bean
    public HystrixMetricsStreamServlet hystrixMetricsStreamServlet() {
        return new HystrixMetricsStreamServlet();
    }

    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> hystrixServlet() {
        HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registration = new ServletRegistrationBean<>(servlet, "/hystrix.stream");
        registration.setName("HystrixServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> actuatorHystrixServlet() {
        HystrixMetricsStreamServlet servlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registration = new ServletRegistrationBean<>(servlet, "/actuator/hystrix.stream");
        registration.setName("ActuatorHystrixServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
