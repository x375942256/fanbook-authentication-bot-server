package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TConfigDao;
import com.example.demo.entity.TConfig;
import com.example.demo.service.TConfigService;
import org.springframework.stereotype.Service;

/**
 * (TConfig)表服务实现类
 *
 * @author makejava
 * @since 2022-03-23 23:19:30
 */
@Service("tConfigService")
public class TConfigServiceImpl extends ServiceImpl<TConfigDao, TConfig> implements TConfigService {

}

