package com.itheima.service;

public interface EmpLogService {
    /**
     * 记录员工操作日志（以独立事务提交，演示 REQUIRES_NEW）
     */
    void insertLog(String info);
}
