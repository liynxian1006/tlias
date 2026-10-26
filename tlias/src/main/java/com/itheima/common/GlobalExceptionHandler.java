package com.itheima.common;

import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * 三层架构中异常自 Mapper 逐层向上抛出，由这里统一捕获并转换成规范的 Result 响应体
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 唯一约束冲突（如 emp.phone 手机号重复、emp.username 用户名重复）
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        String msg = e.getMessage() == null ? "" : e.getMessage();
        log.warn("唯一约束冲突: {}", msg);
        if (msg.contains("uk_emp_phone")) {
            return Result.error("该手机号已被其他员工占用，请更换");
        }
        if (msg.contains("username")) {
            return Result.error("用户名已存在，请更换");
        }
        return Result.error("数据重复，操作失败");
    }

    /**
     * 完整性约束冲突（外键约束等）
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        String msg = e.getMessage() == null ? "" : e.getMessage();
        if (msg.contains("Foreign key constraint")) {
            return Result.error("存在关联数据，无法删除");
        }
        log.warn("完整性约束冲突: {}", msg);
        return Result.error("数据操作冲突，请检查后重试");
    }

    /**
     * 兜底：其余异常统一记录堆栈，仅向前端返回简洁提示
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("操作失败，请联系管理员");
    }
}
