package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 员工操作日志 —— 用于演示 Spring 事务传播行为（REQUIRES_NEW）
 */
@Data
public class EmpLog {
    private Integer id;
    private String operateEmpName;
    private LocalDateTime operateTime;
    private String info;
}
