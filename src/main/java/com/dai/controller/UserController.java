package com.dai.controller;

import com.dai.dto.UserDto;
import com.dai.entity.Result;
import com.dai.service.UserService;
import com.dai.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        UserVo login = userService.login(userDto);
        return Result.success(login);
    }

    @GetMapping("/user/detail")
    private Result getUserDetail() {
        UserVo userDetail = userService.getUserDetail();
        return Result.success(userDetail);
    }
}
