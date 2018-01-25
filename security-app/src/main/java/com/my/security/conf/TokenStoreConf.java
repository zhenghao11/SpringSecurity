package com.my.security.conf;

import com.my.security.jwt.JwtEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @Author hzheng
 * @Date 2017/12/9
 */
@Configuration
public class TokenStoreConf {
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    @ConditionalOnProperty(prefix = "xinge.security.oauth2",name = "tokenStoreType",havingValue = "redis")
    public TokenStore redisToken(){
        return new RedisTokenStore(redisConnectionFactory);
    }

   /* // jwt
    @Configuration
    @ConditionalOnProperty(prefix = "xinge.security.oauth2",name = "tokenStoreType",havingValue = "jwt",matchIfMissing = true)
    public static class jwtTokenConf{
        @Bean
        public TokenStore jwtToken(){
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey("xingeKey");
            return jwtAccessTokenConverter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer(){
            return new JwtEnhancer();
        }
    }*/
}
