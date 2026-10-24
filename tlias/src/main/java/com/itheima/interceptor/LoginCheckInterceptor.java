package com.itheima.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录校验拦截器：除 /login 与静态资源外，所有请求必须携带合法的 token
 */
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            writeUnauthorized(response, "未登录");
            return false;
        }

        try {
            Claims claims = jwtUtils.parseToken(token);
            request.setAttribute("loginId", claims.get("id"));
            request.setAttribute("loginUsername", claims.get("username"));
            return true;
        } catch (Exception e) {
            writeUnauthorized(response, "令牌无效或已过期");
            return false;
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), Result.error(msg));
    }
}
