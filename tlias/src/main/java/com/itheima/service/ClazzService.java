package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface ClazzService {
    PageResult page(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize);
    void deleteById(Integer id);
    void save(Clazz clazz);
    Clazz getById(Integer id);
    void update(Clazz clazz);
    List<Clazz> listAll();
}
