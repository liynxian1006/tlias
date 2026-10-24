package com.itheima.service.impl;

import com.itheima.mapper.ClazzMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Resource
    private ClazzMapper clazzMapper;

    @Override
    public PageResult page(String name, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Clazz> rows = clazzMapper.selectPage(name, begin, end, offset, pageSize);
        Long total = clazzMapper.count(name, begin, end);
        return new PageResult(total, rows);
    }

    @Override
    public void deleteById(Integer id) {
        clazzMapper.deleteById(id);
    }

    @Override
    public void save(Clazz clazz) {
        LocalDateTime now = LocalDateTime.now();
        clazz.setCreateTime(now);
        clazz.setUpdateTime(now);
        clazz.setStatus(computeStatus(clazz));
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazz.setStatus(computeStatus(clazz));
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> listAll() {
        return clazzMapper.selectAll();
    }

    /**
     * 根据开班/结课日期计算班级状态
     */
    private String computeStatus(Clazz clazz) {
        LocalDate now = LocalDate.now();
        if (clazz.getEndDate() != null && now.isAfter(clazz.getEndDate())) {
            return "已结课";
        }
        if (clazz.getBeginDate() != null && now.isBefore(clazz.getBeginDate())) {
            return "未开班";
        }
        return "已开班";
    }
}
