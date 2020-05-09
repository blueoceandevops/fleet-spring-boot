package com.fleet.security.config.adapter;

import com.fleet.security.config.handler.AccessDenied;
import com.fleet.security.config.handler.AuthenticationFailure;
import com.fleet.security.config.handler.AuthenticationSuccess;
import com.fleet.security.config.handler.LogoutSuccess;
import com.fleet.security.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationSuccess authenticationSuccess;

    @Resource
    private AuthenticationFailure authenticationFailure;

    @Resource
    private LogoutSuccess logoutSuccess;

    @Resource
    private AccessDenied accessDenied;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/guest/**", "/notIn", "/in", "/out", "/outError", "/unauth");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/user/get").hasAuthority("USER:GET")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/notIn")
                .loginProcessingUrl("/login").permitAll()
                .usernameParameter("name").passwordParameter("password")
                .successHandler(authenticationSuccess).permitAll()
                .failureHandler(authenticationFailure).permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccess).permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDenied)
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
