package com.dai.mapper;

import com.dai.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserVo getUserByUsername(String username);
}
