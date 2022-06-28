package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.TMobileTemp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TMobileTemp)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-16 12:22:29
 */
@Mapper
public interface TMobileTempDao extends BaseMapper<TMobileTemp> {
    @Insert({
            "<script>",
            "insert into t_mobile_temp(mobile,guild,role_name,role_ids,sheet_no) values ",
            "<foreach collection='tMobileTemps' item='tMobileTemp' index='index' separator=','>",
            "(#{tMobileTemp.mobile},#{tMobileTemp.guild},#{tMobileTemp.roleName},#{tMobileTemp.roleIds},#{tMobileTemp.sheetNo})",
            "</foreach>",
            "</script>"
    })
    void insertBitch(@Param(value = "tMobiles") List<TMobileTemp> tMobileTemps);
}
