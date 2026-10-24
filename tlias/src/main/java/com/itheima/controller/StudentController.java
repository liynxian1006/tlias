package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Integer degree, Integer clazzId) {
        log.info("学员列表查询, name:{}, degree:{}, clazzId:{}", name, degree, clazzId);
        PageResult pageResult = studentService.page(name, degree, clazzId, page, pageSize);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("批量删除学员, ids: {}", ids);
        studentService.deleteByIds(ids);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学员, student: {}", student);
        studentService.save(student);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据id查询学员, id: {}", id);
        return Result.success(studentService.getById(id));
    }

    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学员, student: {}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 违纪处理
     */
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("学员违纪处理, id:{}, score:{}", id, score);
        studentService.handleViolation(id, score);
        return Result.success();
    }
}
