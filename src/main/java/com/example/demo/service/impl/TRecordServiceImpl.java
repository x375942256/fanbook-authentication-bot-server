package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TRecordDao;
import com.example.demo.entity.TRecord;
import com.example.demo.service.TRecordService;
import org.springframework.stereotype.Service;

/**
 * (TRecord)表服务实现类
 *
 * @author makejava
 * @since 2022-04-13 10:46:15
 */
@Service("tRecordService")
public class TRecordServiceImpl extends ServiceImpl<TRecordDao, TRecord> implements TRecordService {

}
