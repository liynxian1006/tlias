package com.itheima.pojo;

import lombok.Data;

/**
 * 登录返回信息
 */
@Data
public class LoginInfo {

    private Integer id;
    private String username;
    private String name;
    private String token;
}
