package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TConfig)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-23 23:19:30
 */
@Mapper
public interface TConfigDao extends BaseMapper<TConfig> {

}

