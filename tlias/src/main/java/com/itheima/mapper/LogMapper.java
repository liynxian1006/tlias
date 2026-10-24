package com.itheima.mapper;

import com.itheima.pojo.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

    @Insert("insert into log(operate_emp_id, operate_time, class_name, method_name, method_params, " +
            "return_value, cost_time, operate_emp_name) values(#{operateEmpId}, #{operateTime}, " +
            "#{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime}, #{operateEmpName})")
    void insert(Log log);

    @Select("select id, operate_emp_id, operate_time, class_name, method_name, method_params, " +
            "return_value, cost_time, operate_emp_name from log order by operate_time desc limit #{offset}, #{pageSize}")
    List<Log> selectPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("select count(*) from log")
    Long count();
}
