package com.itheima.controller;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Resource
    private ClazzService clazzService;

    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, LocalDate begin, LocalDate end) {
        log.info("班级列表查询, name:{}, begin:{}, end:{}", name, begin, end);
        PageResult pageResult = clazzService.page(name, begin, end, page, pageSize);
        return Result.success(pageResult);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除班级, id: {}", id);
        clazzService.deleteById(id);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级, clazz: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据id查询班级, id: {}", id);
        return Result.success(clazzService.getById(id));
    }

    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级, clazz: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        List<Clazz> list = clazzService.listAll();
        return Result.success(list);
    }
}
