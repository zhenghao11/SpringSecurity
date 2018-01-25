package com.my.security.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author hzheng
 * @Date 2017/10/26
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler { // extends SavedRequestAwareAuthenticationSuccessHandler
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        // Authorization        Basic eGluZ2U6eGluZ2VTZWNyZXQ=
        if (header == null || !header.startsWith("Basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头无client信息");
        }
        String[] tokens = extractAndDecodeHeader(header);
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) throw new UnapprovedClientAuthenticationException("clientDetails信息不存在:" + clientId);

        if (StringUtils.isNotBlank(clientDetails.getClientSecret()) && !StringUtils.equals(clientSecret,clientDetails.getClientSecret()))
            throw new UnapprovedClientAuthenticationException("clientId不匹配:" + clientId);

        // 获取tokenRequest
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP,clientId,clientDetails.getScope(),"custom");
        // 获取oauth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        // 用户名
        System.out.println(authentication.getPrincipal());
        // 获取OAuth2Authentication
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request,authentication);

        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        // 将token存到redis
    //    stringRedisTemplate.opsForValue().set(authentication.getPrincipal().toString(),token.getValue(),1800, TimeUnit.SECONDS);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));
    }

    private String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }
}
