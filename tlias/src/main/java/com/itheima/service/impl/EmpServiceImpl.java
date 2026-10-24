package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Resource
    private EmpMapper empMapper;

    @Resource
    private EmpLogService empLogService;

    @Override
    public PageResult page(String name, Integer gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Emp> rows = empMapper.selectPage(name, gender, begin, end, offset, pageSize);
        Long total = empMapper.count(name, gender, begin, end);
        return new PageResult(total, rows);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        empMapper.deleteExprByEmpIds(ids);
        empMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Emp emp) {
        try {
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            if (emp.getPassword() == null || emp.getPassword().isEmpty()) {
                emp.setPassword("123456");
            }
            empMapper.insert(emp);
            if (emp.getExprList() != null && !emp.getExprList().isEmpty()) {
                empMapper.insertExprBatch(emp.getId(), emp.getExprList());
            }
            // 演示事务回滚时可临时放开下一行：int x = 1 / 0;
        } finally {
            // 无论新增成功或回滚，操作日志都以独立事务（REQUIRES_NEW）提交
            empLogService.insertLog("新增员工：" + (emp.getName() == null ? "" : emp.getName()));
        }
    }

    @Override
    public Emp getById(Integer id) {
        Emp emp = empMapper.getById(id);
        if (emp != null) {
            List<EmpExpr> exprList = empMapper.selectExprByEmpId(id);
            emp.setExprList(exprList);
        }
        return emp;
    }

    @Override
    @Transactional
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
        // 工作经历：先删后插，保证数据一致
        empMapper.deleteExprByEmpId(emp.getId());
        if (emp.getExprList() != null && !emp.getExprList().isEmpty()) {
            for (EmpExpr expr : emp.getExprList()) {
                expr.setEmpId(emp.getId());
            }
            empMapper.insertExprBatch(emp.getId(), emp.getExprList());
        }
    }

    @Override
    public List<Emp> listAll() {
        return empMapper.selectAll();
    }
}
