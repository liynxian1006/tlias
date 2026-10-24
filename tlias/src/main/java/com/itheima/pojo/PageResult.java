package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询统一返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {

    private Long total; // 总记录数
    private List rows;  // 数据列表
}
