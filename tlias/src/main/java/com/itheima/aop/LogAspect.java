package com.itheima.aop;

import com.itheima.mapper.LogMapper;
import com.itheima.pojo.Log;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面：环绕通知记录所有业务 Controller 方法的调用信息
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private LogMapper logMapper;

    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtUtils jwtUtils;

    @Around("execution(* com.itheima.controller.*.*(..)) && !within(com.itheima.controller.LogController)")
    public Object recordLog(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long cost = System.currentTimeMillis() - start;

        try {
            Signature signature = pjp.getSignature();
            String className = pjp.getTarget().getClass().getName();
            String methodName = signature.getName();
            String params = Arrays.toString(pjp.getArgs());
            String returnValue = result != null ? result.toString() : "null";
            if (returnValue.length() > 2000) {
                returnValue = returnValue.substring(0, 2000);
            }

            Integer empId = null;
            String token = request.getHeader("token");
            if (token != null) {
                try {
                    Claims claims = jwtUtils.parseToken(token);
                    Object id = claims.get("id");
                    if (id instanceof Integer) {
                        empId = (Integer) id;
                    } else if (id instanceof Number) {
                        empId = ((Number) id).intValue();
                    }
                } catch (Exception ignored) {
                }
            }

            Log logEntity = new Log();
            logEntity.setOperateEmpId(empId);
            logEntity.setOperateTime(LocalDateTime.now());
            logEntity.setClassName(className);
            logEntity.setMethodName(methodName);
            logEntity.setMethodParams(params);
            logEntity.setReturnValue(returnValue);
            logEntity.setCostTime(cost);
            logMapper.insert(logEntity);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }

        return result;
    }
}
