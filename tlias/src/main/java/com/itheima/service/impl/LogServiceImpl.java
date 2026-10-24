package com.itheima.service.impl;

import com.itheima.mapper.LogMapper;
import com.itheima.pojo.Log;
import com.itheima.pojo.PageResult;
import com.itheima.service.LogService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public PageResult page(Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Log> rows = logMapper.selectPage(offset, pageSize);
        Long total = logMapper.count();
        return new PageResult(total, rows);
    }

    @Override
    public void save(Log log) {
        logMapper.insert(log);
    }
}
