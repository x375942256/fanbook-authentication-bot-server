package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TWord;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TWord)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-13 10:34:08
 */
@Mapper
public interface TWordDao extends BaseMapper<TWord> {

}
