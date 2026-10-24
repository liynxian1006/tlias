package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {

    List<Emp> selectPage(@Param("name") String name, @Param("gender") Integer gender,
                         @Param("begin") LocalDate begin, @Param("end") LocalDate end,
                         @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    Long count(@Param("name") String name, @Param("gender") Integer gender,
               @Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, password, name, gender, image, job, salary, entry_date, " +
            "dept_id, create_time, update_time) values(#{username}, #{password}, #{name}, #{gender}, " +
            "#{image}, #{job}, #{salary}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    void insertExprBatch(@Param("empId") Integer empId, @Param("list") List<EmpExpr> list);

    void deleteByIds(@Param("ids") List<Integer> ids);

    void deleteExprByEmpIds(@Param("ids") List<Integer> ids);

    @Select("select id, username, password, name, gender, image, job, salary, entry_date, dept_id, " +
            "create_time, update_time from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    Emp getById(Integer id);

    List<EmpExpr> selectExprByEmpId(Integer empId);

    void update(Emp emp);

    void updateExprBatch(@Param("list") List<EmpExpr> list);

    void deleteExprByEmpId(Integer empId);

    @Select("select id, username, password, name, gender, image, job, salary, entry_date, dept_id, " +
            "create_time, update_time from emp order by id")
    @Results({
            @Result(column = "entry_date", property = "entryDate"),
            @Result(column = "dept_id", property = "deptId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Emp> selectAll();
}
