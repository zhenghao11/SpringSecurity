package com.my.security.conf;

import com.my.security.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @Author hzheng
 * @Date 2017/12/8
 */
@Configuration
@EnableResourceServer // 资源服务器
public class ResourcesServer extends ResourceServerConfigurerAdapter {

    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    MyAuthenticationFailHandler myAuthenticationFailHandler;


 /*   @Bean
    public PasswordEncoder encode() {
        return new Md5Util();
    }*/

    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginProcessingUrl("/testLogin")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailHandler);

        http.authorizeRequests()
                .antMatchers("/testLogin").permitAll()
                .anyRequest() // 除此之外的任何请求都需要认证
                .authenticated()
                .and()
                .csrf().disable();
    }

}
