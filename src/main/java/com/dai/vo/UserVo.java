package com.dai.vo;

import lombok.Data;

@Data
public class UserVo {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private Integer sex;

    private String token;

}
