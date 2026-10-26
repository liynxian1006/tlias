package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 员工信息统计结果封装（职位统计 / 班级人数统计）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOption {

    /** 分类名称列表，如：班主任、讲师、学工主管…… */
    private List jobList;

    /** 与 jobList 一一对应的数量列表 */
    private List dataList;
}
