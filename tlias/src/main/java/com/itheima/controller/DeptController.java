package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Resource
    private DeptService deptService;

    /**
     * 查询部门列表
     */
    @GetMapping
    public Result list() {
        log.info("查询部门列表");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /**
     * 根据 id 删除部门
     */
    @DeleteMapping
    public Result delete(Integer id) {
        log.info("根据id删除部门, id: {}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    /**
     * 新增部门
     */
    @PostMapping
    public Result save(@RequestBody Dept dept) {
        log.info("新增部门, dept: {}", dept);
        deptService.save(dept);
        return Result.success();
    }

    /**
     * 根据 id 查询部门
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据id查询部门, id: {}", id);
        return Result.success(deptService.getById(id));
    }

    /**
     * 修改部门
     */
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门, dept: {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}
