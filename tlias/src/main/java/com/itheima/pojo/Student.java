package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学员实体
 */
@Data
public class Student {

    private Integer id;
    private String name;
    private String no;
    private Integer gender;     // 1 男, 2 女
    private String phone;
    private Integer degree;     // 1 初中,2 高中,3 大专,4 本科,5 硕士,6 博士
    private String idCard;
    private Integer isCollege;  // 1 是, 0 否
    private String address;
    private LocalDate graduationDate;
    private Integer violationCount;
    private Integer violationScore;
    private Integer clazzId;
    private String clazzName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
