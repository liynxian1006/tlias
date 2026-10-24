package com.itheima.mapper;

import com.itheima.pojo.EmpLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpLogMapper {

    @Insert("insert into emp_log(operate_emp_name, operate_time, info) " +
            "values(#{operateEmpName}, #{operateTime}, #{info})")
    void insert(EmpLog empLog);
}
