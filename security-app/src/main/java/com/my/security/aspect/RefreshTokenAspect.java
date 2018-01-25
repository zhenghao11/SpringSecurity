package com.my.security.aspect;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author hzheng
 * @Date 2017/12/13
 */
@Aspect
@Component
public class RefreshTokenAspect {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Pointcut("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public void refreshToken(){

    }

    @Around("refreshToken()")
    public Object doRefreshToken(ProceedingJoinPoint joinPoint){
        Map<String, String> parameters = (Map<String, String>) joinPoint.getArgs()[1];
        ResponseEntity<OAuth2AccessToken> proceed = null;
        try {
            proceed = (ResponseEntity<OAuth2AccessToken>) joinPoint.proceed();
            if (parameters.get("grant_type").toString().equals("refresh_type")) {
                // 刷新令牌之后得到的access_token
                OAuth2AccessToken token = proceed.getBody();

                // 用户名
                Claims claims = Jwts.parser().setSigningKey("xingeKey" .getBytes("UTF-8")).parseClaimsJws(token.getValue()).getBody();
                String username = claims.get("user_name").toString();
                stringRedisTemplate.opsForValue().set(username, token.getValue(), 1800, TimeUnit.SECONDS);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
