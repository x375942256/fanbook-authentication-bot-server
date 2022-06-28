package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.TWordDao;
import com.example.demo.entity.TWord;
import com.example.demo.service.TWordService;
import org.springframework.stereotype.Service;

/**
 * (TWord)表服务实现类
 *
 * @author makejava
 * @since 2022-04-13 10:34:10
 */
@Service("tWordService")
public class TWordServiceImpl extends ServiceImpl<TWordDao, TWord> implements TWordService {

}
