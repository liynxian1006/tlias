package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 员工工作经历实体
 */
@Data
public class EmpExpr {

    private Integer id;
    private Integer empId;
    private String company;
    private String job;
    private LocalDate begin;
    private LocalDate end;
}
