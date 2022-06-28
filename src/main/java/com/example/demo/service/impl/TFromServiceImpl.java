package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TFromDao;
import com.example.demo.entity.TFrom;
import com.example.demo.service.TFromService;
import org.springframework.stereotype.Service;

/**
 * (TFrom)表服务实现类
 *
 * @author makejava
 * @since 2022-03-23 23:20:17
 */
@Service("tFromService")
public class TFromServiceImpl extends ServiceImpl<TFromDao, TFrom> implements TFromService {
}

