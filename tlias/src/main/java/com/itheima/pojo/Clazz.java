package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 班级实体
 */
@Data
public class Clazz {

    private Integer id;
    private String name;
    private String room;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Integer masterId;   // 班主任(员工ID)
    private String masterName;
    private Integer subject;    // 1:java,2:前端,3:大数据,4:Python,5:Go,6:嵌入式
    private String status;      // 未开班、已开班、已结课
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
