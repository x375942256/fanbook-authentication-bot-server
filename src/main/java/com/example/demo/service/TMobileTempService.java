package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.TMobileTemp;

import java.util.List;

/**
 * (TMobileTemp)表服务接口
 *
 * @author makejava
 * @since 2022-04-16 12:22:30
 */
public interface TMobileTempService extends IService<TMobileTemp> {
    void insertBitch(List<TMobileTemp> tMobileTemps);
}
