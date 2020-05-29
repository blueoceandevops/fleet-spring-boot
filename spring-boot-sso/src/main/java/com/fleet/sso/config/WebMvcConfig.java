package com.fleet.sso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fleet.sso.config.interceptor.TokenAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TokenAuthInterceptor())
				/** 被拦截的链接 */
				.addPathPatterns("/**")
				/** 不会被拦截的链接 */
				.excludePathPatterns("/signUp", "/login", "/logout", "/user/**", "/redis/**", "/**");
	}

}
