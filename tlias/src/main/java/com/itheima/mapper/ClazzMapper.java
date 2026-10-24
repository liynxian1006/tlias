package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClazzMapper {

    List<Clazz> selectPage(@Param("name") String name, @Param("begin") LocalDate begin,
                           @Param("end") LocalDate end, @Param("offset") int offset,
                           @Param("pageSize") int pageSize);

    Long count(@Param("name") String name, @Param("begin") LocalDate begin, @Param("end") LocalDate end);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into clazz(name, room, begin_date, end_date, master_id, subject, status, " +
            "create_time, update_time) values(#{name}, #{room}, #{beginDate}, #{endDate}, " +
            "#{masterId}, #{subject}, #{status}, #{createTime}, #{updateTime})")
    void insert(Clazz clazz);

    void deleteById(Integer id);

    Clazz getById(Integer id);

    void update(Clazz clazz);

    @Select("select id, name, room, begin_date, end_date, master_id, subject, status, " +
            "create_time, update_time from clazz order by id")
    @Results({
            @Result(column = "begin_date", property = "beginDate"),
            @Result(column = "end_date", property = "endDate"),
            @Result(column = "master_id", property = "masterId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<Clazz> selectAll();
}
