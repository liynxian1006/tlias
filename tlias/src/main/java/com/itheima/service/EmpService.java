package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    PageResult page(String name, Integer gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize);
    void deleteByIds(List<Integer> ids);
    void save(Emp emp);
    Emp getById(Integer id);
    void update(Emp emp);
    List<Emp> listAll();
}
