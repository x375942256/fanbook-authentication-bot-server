package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TFrom;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TFrom)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-23 23:20:17
 */
@Mapper
public interface TFromDao extends BaseMapper<TFrom> {
    @Delete("delete from t_from")
    public Integer del();
}

