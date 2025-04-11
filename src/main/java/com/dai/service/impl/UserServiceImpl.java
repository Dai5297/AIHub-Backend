package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.dto.UserDto;
import com.dai.mapper.UserMapper;
import com.dai.properties.JwtTokenManagerProperties;
import com.dai.service.UserService;
import com.dai.utils.JwtUtil;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final JwtTokenManagerProperties jwtTokenManagerProperties;

    @Override
    public UserVo login(UserDto userDto) {
        UserVo userVo = userMapper.getUserByUsername(userDto.getUsername());
        String jsonStr = JSONUtil.toJsonStr(userVo);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", jsonStr);
        String jwt = JwtUtil.createJWT(jwtTokenManagerProperties.getBase64EncodedSecretKey(), jwtTokenManagerProperties.getTtl(), claims);
        userVo.setToken(jwt);
        return userVo;
    }

    @Override
    public UserVo getUserDetail() {
        String subject = UserThreadLocal.getSubject();
        return JSONUtil.toBean(subject, UserVo.class);
    }
}
