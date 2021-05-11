package com.csse433.blackboard.rdbms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csse433.blackboard.rdbms.entity.MessageMongoBak;
import com.csse433.blackboard.rdbms.entity.RedisBak;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Henry Yang
 * @since 2021-05-05
 */
@Mapper
public interface RedisBakMapper extends BaseMapper<RedisBak> {


    // List<String> getByName(@Param("name") String name);
}
