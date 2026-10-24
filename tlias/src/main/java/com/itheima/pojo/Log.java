package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体
 */
@Data
public class Log {

    private Integer id;
    private Integer operateEmpId;
    private LocalDateTime operateTime;
    private String className;
    private String methodName;
    private String methodParams;
    private String returnValue;
    private Long costTime;
    private String operateEmpName;
}
