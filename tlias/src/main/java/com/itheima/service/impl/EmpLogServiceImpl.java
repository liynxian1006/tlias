package com.itheima.service.impl;

import com.itheima.mapper.EmpLogMapper;
import com.itheima.pojo.EmpLog;
import com.itheima.service.EmpLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Resource
    private EmpLogMapper empLogMapper;

    /**
     * 事务传播行为：REQUIRES_NEW
     * 无论外部事务是否回滚，本方法都会挂起外部事务、开启一个全新事务并独立提交。
     * 用途：演示“新增员工即便失败回滚，操作日志仍要落库”的场景。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void insertLog(String info) {
        EmpLog empLog = new EmpLog();
        empLog.setOperateEmpName("admin");
        empLog.setOperateTime(LocalDateTime.now());
        empLog.setInfo(info);
        empLogMapper.insert(empLog);
    }
}
