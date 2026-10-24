package com.itheima.service.impl;

import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.service.StudentService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Override
    public PageResult page(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Student> rows = studentMapper.selectPage(name, degree, clazzId, offset, pageSize);
        Long total = studentMapper.count(name, degree, clazzId);
        return new PageResult(total, rows);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        studentMapper.deleteByIds(ids);
    }

    @Override
    public void save(Student student) {
        LocalDateTime now = LocalDateTime.now();
        student.setCreateTime(now);
        student.setUpdateTime(now);
        if (student.getViolationCount() == null) {
            student.setViolationCount(0);
        }
        if (student.getViolationScore() == null) {
            student.setViolationScore(0);
        }
        studentMapper.insert(student);
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void handleViolation(Integer id, Integer score) {
        Student student = studentMapper.getById(id);
        if (student == null) {
            return;
        }
        student.setViolationCount(student.getViolationCount() == null ? 1 : student.getViolationCount() + 1);
        student.setViolationScore(student.getViolationScore() == null ? score : student.getViolationScore() + score);
        studentMapper.update(student);
    }
}
