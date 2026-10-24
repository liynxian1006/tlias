package com.itheima.mapper;

import com.itheima.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

    List<Student> selectPage(@Param("name") String name, @Param("degree") Integer degree,
                             @Param("clazzId") Integer clazzId, @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    Long count(@Param("name") String name, @Param("degree") Integer degree, @Param("clazzId") Integer clazzId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into student(name, no, gender, phone, degree, id_card, is_college, address, " +
            "graduation_date, violation_count, violation_score, clazz_id, create_time, update_time) " +
            "values(#{name}, #{no}, #{gender}, #{phone}, #{degree}, #{idCard}, #{isCollege}, #{address}, " +
            "#{graduationDate}, #{violationCount}, #{violationScore}, #{clazzId}, #{createTime}, #{updateTime})")
    void insert(Student student);

    void deleteByIds(@Param("ids") List<Integer> ids);

    Student getById(Integer id);

    void update(Student student);

    @Select("select id, name, no, gender, phone, degree, id_card, is_college, address, " +
            "graduation_date, violation_count, violation_score, clazz_id, create_time, update_time " +
            "from student order by id")
    @Results({
            @Result(column = "id_card", property = "idCard"),
            @Result(column = "is_college", property = "isCollege"),
            @Result(column = "graduation_date", property = "graduationDate"),
            @Result(column = "violation_count", property = "violationCount"),
            @Result(column = "violation_score", property = "violationScore"),
            @Result(column = "clazz_id", property = "clazzId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Student> selectAll();
}
