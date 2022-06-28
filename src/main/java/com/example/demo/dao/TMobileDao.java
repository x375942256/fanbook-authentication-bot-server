package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Role;
import com.example.demo.entity.TMobile;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * (TMobile)表数据库访问层
 *
 * @author makejava
 * @since 2022-04-11 20:43:40
 */
@Mapper
public interface TMobileDao extends BaseMapper<TMobile> {

    @Select("SELECT t1.* FROM `t_mobile_temp` t1 left join t_mobile t2 on t1.mobile=t2.mobile and t1.guild=t2.guild and t1.sheet_no=t2.sheet_no and t1.role_name <=> t2.role_name  where t1.guild=#{guild} and t1.sheet_no=#{sheetNo} and t2.id is null")
    List<TMobile> getAddList(@Param("guild") Long guild, @Param("sheetNo") Integer sheetNo);

    @Select("SELECT t1.* FROM `t_mobile` t1 left join t_mobile_temp t2 on t1.mobile=t2.mobile and t1.guild=t2.guild and t1.sheet_no=t2.sheet_no and t1.role_name <=> t2.role_name where t1.guild=#{guild} and t1.sheet_no=#{sheetNo} and t2.id is null")
    List<TMobile> getDelList(@Param("guild") Long guild, @Param("sheetNo") Integer sheetNo);

    @Insert({
            "<script>",
            "insert into t_mobile(mobile,guild,role_name,role_ids,sheet_no) values ",
            "<foreach collection='tMobiles' item='tMobile' index='index' separator=','>",
            "(#{tMobile.mobile},#{tMobile.guild},#{tMobile.roleName},#{tMobile.roleIds},#{tMobile.sheetNo})",
            "</foreach>",
            "</script>"
    })
    void insertBitch(@Param(value = "tMobiles") List<TMobile> tMobiles);


    @Select("SELECT role_name FROM `t_mobile` GROUP BY role_name")
    List<Role> getRoleGroup();

    @Select("SELECT * FROM `t_mobile` WHERE role_name = #{role_name}")
    List<TMobile> getTMobileList(@Param("role_name") String roleName);

    @Delete("delete from t_mobile where id not in (select t.max_id from (select max(id) as max_id FROM t_mobile group by mobile,role_name,guild,sheet_no) as t)")
    void clear();

    @Delete("delete from t_mobile where guild = #{guild} and sheet_no >= #{size}")
    void deleteBySheet(@Param("guild") Long guildId,@Param("size") Integer size);
}
