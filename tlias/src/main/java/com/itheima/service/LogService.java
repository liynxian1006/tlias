package com.itheima.service;

import com.itheima.pojo.Log;
import com.itheima.pojo.PageResult;

import java.util.List;

public interface LogService {
    PageResult page(Integer page, Integer pageSize);
    void save(Log log);
}
