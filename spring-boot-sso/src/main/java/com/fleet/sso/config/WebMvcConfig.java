package com.fleet.sso.config;

import com.fleet.sso.config.interceptor.TokenInterceptor;
import com.fleet.sso.config.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns());
        registry.addInterceptor(userInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public TokenInterceptor tokenInterceptor() {
        List<String> patternList = new ArrayList<>();
        patternList.add("/token/refresh");
        return new TokenInterceptor(patternList);
    }

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    public List<String> excludePathPatterns() {
        List<String> patterns = new ArrayList<>();
        patterns.add("/login");
        patterns.add("/signUp");
        patterns.add("/guest/**");
        return patterns;
    }
}
