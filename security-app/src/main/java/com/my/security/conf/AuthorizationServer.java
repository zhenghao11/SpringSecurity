package com.my.security.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hzheng
 * @Date 2017/12/7
 */
@Configuration
@EnableAuthorizationServer  // 认证服务器
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter{
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenStore tokenStore;

    @Autowired(required = false)
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    TokenEnhancer jwtTokenEnhancer;

    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
                 .authenticationManager(authenticationManager)
                 .userDetailsService(userDetailsService);
        // jwt 和 jwt增强器
        if (null != jwtAccessTokenConverter && null != jwtTokenEnhancer){
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

            List<TokenEnhancer> tokenEnhancerList = new ArrayList<TokenEnhancer>();
            tokenEnhancerList.add(jwtTokenEnhancer);
            tokenEnhancerList.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);

            endpoints.tokenEnhancer(tokenEnhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("xinge") // clientId 和 clientSecret 保存在内存
                .secret("xingeSecret")
                .accessTokenValiditySeconds(300) // access_token存活时间
                .authorizedGrantTypes("refresh_token","password"/*,"authorization_code"*/)
                .scopes("all")
                .refreshTokenValiditySeconds(3600); // refresh_token存活时间
    }

}
