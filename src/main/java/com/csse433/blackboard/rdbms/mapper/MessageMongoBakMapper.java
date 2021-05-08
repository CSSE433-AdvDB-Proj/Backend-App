package com.csse433.blackboard.rdbms.mapper;

import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Chet Zhang
 * @since 2021-05-05
 */
@Mapper
public interface MessageMongoBakMapper extends BaseMapper<MessageMongoBak> {


    List<String> getByName(@Param("name") String name);
}
