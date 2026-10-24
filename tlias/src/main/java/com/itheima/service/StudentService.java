package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;

import java.util.List;

public interface StudentService {
    PageResult page(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize);
    void deleteByIds(List<Integer> ids);
    void save(Student student);
    Student getById(Integer id);
    void update(Student student);
    void handleViolation(Integer id, Integer score);
}
