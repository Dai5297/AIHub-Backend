package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.dto.UserDto;
import com.dai.mapper.UserMapper;
import com.dai.properties.JwtTokenManagerProperties;
import com.dai.service.UserService;
import com.dai.utils.JwtUtil;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenManagerProperties jwtTokenManagerProperties;

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
        UserVo userVo = JSONUtil.toBean(subject, UserVo.class);
        return userVo;
    }
}
