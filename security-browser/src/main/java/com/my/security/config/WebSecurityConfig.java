package com.my.security.config;

import com.my.security.conf.MyAuthenticationFailHandler;
import com.my.security.conf.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author hzheng
 * @Date 2017/10/25
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailHandler myAuthenticationFailHandler;
    @Autowired
    OverrideExpiredSessionStrategy overrideExpiredSessionStrategy;

  /*  @Bean
    public PasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/authorize/form")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailHandler)
            .and()
                .sessionManagement()
                .invalidSessionUrl("/session/timeout")
                .maximumSessions(1)   // 踢掉前者
             //   .maxSessionsPreventsLogin(true)  不让后者登录
                .expiredSessionStrategy(overrideExpiredSessionStrategy)
            .and()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login.html")
                .and()
                .authorizeRequests()
                .antMatchers("/login.html","/byebye.html","/authorize/form","/session/timeout").permitAll()
                .anyRequest() // 除此之外的任何请求都需要认证
                .authenticated()
            .and()
                .csrf().disable();

    }
}
