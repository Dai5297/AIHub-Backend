package com.dai.intercept;

import cn.hutool.core.util.ObjectUtil;
import com.dai.properties.JwtTokenManagerProperties;
import com.dai.utils.JwtUtil;
import com.dai.utils.UserThreadLocal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserTokenIntercept implements HandlerInterceptor {

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()){
            return false;
        }
        Claims claims = JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), token);
        if (!ObjectUtil.isEmpty(claims) && !ObjectUtil.isNull(claims)){
            String jsonStr = String.valueOf(claims.get("user"));
            UserThreadLocal.setSubject(jsonStr);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.removeSubject();
    }
}
