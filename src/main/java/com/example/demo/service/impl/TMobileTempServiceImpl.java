package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TMobileTempDao;
import com.example.demo.entity.TMobileTemp;
import com.example.demo.service.TMobileTempService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (TMobileTemp)表服务实现类
 *
 * @author makejava
 * @since 2022-04-16 12:22:30
 */
@Service("tMobileTempService")
public class TMobileTempServiceImpl extends ServiceImpl<TMobileTempDao, TMobileTemp> implements TMobileTempService {

    @Override
    public void insertBitch(List<TMobileTemp> tMobileTemps) {
        this.baseMapper.insertBitch(tMobileTemps);
    }
}
