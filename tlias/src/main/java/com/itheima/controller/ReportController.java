package com.itheima.controller;

import com.itheima.mapper.ReportMapper;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.*;

/**
 * 数据统计
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportMapper reportMapper;

    private static final Map<Integer, String> JOB_MAP = new LinkedHashMap<>();

    static {
        JOB_MAP.put(1, "班主任");
        JOB_MAP.put(2, "讲师");
        JOB_MAP.put(3, "学工主管");
        JOB_MAP.put(4, "教研主管");
        JOB_MAP.put(5, "咨询师");
    }

    /**
     * 员工性别统计
     */
    @GetMapping("/empGenderData")
    public Result empGenderData() {
        return Result.success(reportMapper.empGenderData());
    }

    /**
     * 员工职位人数统计
     */
    @GetMapping("/empJobData")
    public Result empJobData() {
        List<Map<String, Object>> raw = reportMapper.empJobRaw();
        List<String> jobList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();
        // 先保证职位顺序与文档一致
        Map<Integer, Integer> rawMap = new HashMap<>();
        for (Map<String, Object> m : raw) {
            Object job = m.get("job");
            Object cnt = m.get("cnt");
            Integer jobKey = job instanceof Number ? ((Number) job).intValue() : null;
            Integer count = cnt instanceof Number ? ((Number) cnt).intValue() : 0;
            if (jobKey != null) {
                rawMap.put(jobKey, count);
            }
        }
        for (Map.Entry<Integer, String> entry : JOB_MAP.entrySet()) {
            jobList.add(entry.getValue());
            dataList.add(rawMap.getOrDefault(entry.getKey(), 0));
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("jobList", jobList);
        result.put("dataList", dataList);
        return Result.success(result);
    }

    /**
     * 学员学历统计
     */
    @GetMapping("/studentDegreeData")
    public Result studentDegreeData() {
        return Result.success(reportMapper.studentDegreeData());
    }

    /**
     * 班级人数统计
     */
    @GetMapping("/studentCountData")
    public Result studentCountData() {
        List<Map<String, Object>> raw = reportMapper.studentCountRaw();
        List<String> clazzList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();
        for (Map<String, Object> m : raw) {
            clazzList.add(String.valueOf(m.get("clazz")));
            Object cnt = m.get("cnt");
            dataList.add(cnt instanceof Number ? ((Number) cnt).intValue() : 0);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("clazzList", clazzList);
        result.put("dataList", dataList);
        return Result.success(result);
    }
}
