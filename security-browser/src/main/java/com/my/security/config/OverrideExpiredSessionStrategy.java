package com.my.security.config;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author hzheng
 * @Date 2017/11/15
 */
@Component
public class OverrideExpiredSessionStrategy implements SessionInformationExpiredStrategy{

    public void onExpiredSessionDetected(SessionInformationExpiredEvent eventØ) throws IOException, ServletException {
        HttpServletResponse response = eventØ.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("您的账号在他处登录,被迫下线");
    }
}
