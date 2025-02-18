package com.work.psychological.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.psychological.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/* 
 * 用户Mapper
 */
public interface UserMapper extends BaseMapper<User> {
}