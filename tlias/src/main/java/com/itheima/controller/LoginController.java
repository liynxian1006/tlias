package com.itheima.controller;

import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Resource
    private EmpMapper empMapper;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 员工登录，成功后下发 JWT 令牌
     */
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("员工登录, username: {}", emp.getUsername());
        Emp dbEmp = empMapper.selectByUsernameAndPassword(emp.getUsername(), emp.getPassword());
        if (dbEmp == null) {
            return Result.error("用户名或密码错误");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", dbEmp.getId());
        claims.put("username", dbEmp.getUsername());
        String token = jwtUtils.generateToken(claims);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(dbEmp.getId());
        loginInfo.setUsername(dbEmp.getUsername());
        loginInfo.setName(dbEmp.getName());
        loginInfo.setToken(token);
        return Result.success(loginInfo);
    }
}
