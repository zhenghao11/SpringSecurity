package com.my.security.jwt;

import com.my.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * jwt增强器
 * @Author hzheng
 * @Date 2017/12/12
 */
@Component
public class JwtEnhancer implements TokenEnhancer{
    @Autowired
    UserService userService;

    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        try {
            String username = authentication.getUserAuthentication().getPrincipal().toString();
            Map<String,Object> info = new HashMap<String, Object>();
            info.put("company","xingejinfu");
            info.put("user",userService.selectByUsername(username));
            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }
}
