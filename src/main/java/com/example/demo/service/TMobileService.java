package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Role;
import com.example.demo.entity.TMobile;

import java.util.List;

/**
 * (TMobile)表服务接口
 *
 * @author makejava
 * @since 2022-04-11 20:43:40
 */
public interface TMobileService extends IService<TMobile> {

    //从临时表中获取数据，添加、删除主表数据
    void operation(Long guild,Integer sheetNo);

    // 获取角色分组
    List<Role> getRoleGroup();

    List<TMobile> getTMobileList(String roleName);


    void deleteBySheet(Long guildId,Integer size);


    void clear();
}
