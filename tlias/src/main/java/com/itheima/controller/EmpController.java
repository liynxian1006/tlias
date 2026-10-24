package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Resource
    private EmpService empService;

    /**
     * 员工列表条件分页查询
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Integer gender, LocalDate begin, LocalDate end) {
        log.info("员工列表查询, page:{}, pageSize:{}, name:{}, gender:{}, begin:{}, end:{}",
                page, pageSize, name, gender, begin, end);
        PageResult pageResult = empService.page(name, gender, begin, end, page, pageSize);
        return Result.success(pageResult);
    }

    /**
     * 批量删除员工
     */
    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Integer> ids) {
        log.info("批量删除员工, ids: {}", ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工, emp: {}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 根据 id 查询员工（含工作经历）
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据id查询员工, id: {}", id);
        return Result.success(empService.getById(id));
    }

    /**
     * 修改员工
     */
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工, emp: {}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 查询全部员工
     */
    @GetMapping("/list")
    public Result list() {
        return Result.success(empService.listAll());
    }
}
