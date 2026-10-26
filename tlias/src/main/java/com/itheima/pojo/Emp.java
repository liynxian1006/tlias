package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工实体
 */
@Data
public class Emp {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer gender;     // 1 男, 2 女
    private String phone;       // 手机号（唯一约束，重复时触发统一异常处理）
    private String image;
    private Integer job;        // 1 班主任,2 讲师,3 学工主管,4 教研主管,5 咨询师
    private Integer salary;
    private LocalDate entryDate;
    private Integer deptId;
    private String deptName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private List<EmpExpr> exprList; // 工作经历列表
}
