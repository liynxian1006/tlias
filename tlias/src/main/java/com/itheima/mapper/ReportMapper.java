package com.itheima.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    @Select("select case gender when 1 then '男性员工' when 2 then '女性员工' else '其他' end as name, " +
            "count(*) as value from emp group by gender")
    List<Map<String, Object>> empGenderData();

    @Select("select job, count(*) as cnt from emp group by job order by cnt desc")
    List<Map<String, Object>> empJobRaw();

    @Select("select case degree when 1 then '初中' when 2 then '高中' when 3 then '大专' " +
            "when 4 then '本科' when 5 then '硕士' when 6 then '博士' else '其他' end as name, " +
            "count(*) as value from student group by degree")
    List<Map<String, Object>> studentDegreeData();

    @Select("select c.name as clazz, count(*) as cnt from student s join clazz c on s.clazz_id = c.id " +
            "group by c.id, c.name")
    List<Map<String, Object>> studentCountRaw();
}
