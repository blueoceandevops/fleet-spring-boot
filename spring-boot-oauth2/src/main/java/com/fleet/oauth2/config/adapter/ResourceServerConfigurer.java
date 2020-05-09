package com.fleet.oauth2.config.adapter;

import com.fleet.oauth2.config.handler.AccessDenied;
import com.fleet.oauth2.config.handler.AuthEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Resource
    private AuthEntryPoint authEntryPoint;

    @Resource
    private AccessDenied accessDenied;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.accessDeniedHandler(accessDenied)
                .authenticationEntryPoint(authEntryPoint);
        resources.resourceId("resource_id").stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
