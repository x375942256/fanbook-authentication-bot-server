package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * (TRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-13 10:46:14
 */
@Mapper
public interface TRecordDao extends BaseMapper<TRecord> {

}
