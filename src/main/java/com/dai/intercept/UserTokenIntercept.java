package com.dai.intercept;

import cn.hutool.core.util.ObjectUtil;
import com.dai.properties.JwtTokenManagerProperties;
import com.dai.utils.JwtUtil;
import com.dai.utils.UserThreadLocal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserTokenIntercept implements HandlerInterceptor {

    private final JwtTokenManagerProperties jwtTokenManagerProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()){
            response.setStatus(401);
            return false;
        }
        try {
            Claims claims = JwtUtil.parseJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), token);
            if (!ObjectUtil.isEmpty(claims) && !ObjectUtil.isNull(claims)){
                String jsonStr = String.valueOf(claims.get("user"));
                UserThreadLocal.setSubject(jsonStr);
            }
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        UserThreadLocal.removeSubject();
    }
}
