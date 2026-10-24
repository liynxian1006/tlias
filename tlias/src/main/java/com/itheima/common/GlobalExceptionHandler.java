package com.itheima.common;

import com.itheima.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        // 例如删除部门时存在关联员工（外键约束）
        String msg = e.getMessage();
        if (msg != null && msg.contains("Foreign key constraint")) {
            return Result.error("存在关联数据，无法删除");
        }
        return Result.error("数据操作冲突：" + (msg == null ? "" : msg));
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.error("操作失败：" + e.getMessage());
    }
}
