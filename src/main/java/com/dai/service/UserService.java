package com.dai.service;

import com.dai.dto.UserDto;
import com.dai.vo.UserVo;

public interface UserService {

    UserVo login(UserDto userDto);

    UserVo getUserDetail();
}
