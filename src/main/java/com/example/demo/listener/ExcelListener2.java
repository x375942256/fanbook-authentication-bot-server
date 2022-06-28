package com.example.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.TMobileTemp;
import com.example.demo.service.FanBookService;
import com.example.demo.service.TMobileService;
import com.example.demo.service.TMobileTempService;
import com.example.demo.service.TRecordService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelListener2 implements ReadListener<TMobileTemp> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 10000;
    /**
     * 缓存的数据
     */
    private List<TMobileTemp> cachedDataList = new ArrayList<>();
    FanBookService fanBookService;
    TMobileService tMobileService;
    TRecordService recordService;
    TMobileTempService mobileTempService;
    Long guild;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */

    public ExcelListener2(Long guild, TMobileService tMobileService, FanBookService fanBookService, TRecordService recordService, TMobileTempService mobileTempService) {
        this.guild = guild;
        this.tMobileService = tMobileService;
        this.fanBookService = fanBookService;
        this.recordService = recordService;
        this.mobileTempService = mobileTempService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(TMobileTemp data, AnalysisContext context) {
        if (null != data && null != data.getMobile()) {
            //设置服务器ID
            data.setGuild(guild);
            //设置sheet序号
            data.setSheetNo(context.readSheetHolder().getSheetNo());
            cachedDataList.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
                log.info("保存{}条", cachedDataList.size());
                saveTempTable();
                // 存储完成清理 list
                cachedDataList = Lists.newArrayListWithExpectedSize(BATCH_COUNT);
            }
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 确保最后遗留的数据也存储到数据库
        log.info("{}服务器第{}页读取完成 size:{}", guild, context.readSheetHolder().getSheetNo(), cachedDataList.size());
        if (cachedDataList.size() > 0) {
            saveTempTable();
            cachedDataList = Lists.newArrayListWithExpectedSize(BATCH_COUNT);
        }
        List<TMobileTemp> tMobileTempList = mobileTempService.list(Wrappers.<TMobileTemp>lambdaQuery().eq(TMobileTemp::getGuild, guild).eq(TMobileTemp::getSheetNo, context.readSheetHolder().getSheetNo()));
        if (tMobileTempList.size() > 0) {
            //list数量大于0执行
            tMobileService.operation(guild, context.readSheetHolder().getSheetNo());
        } else {
            log.info("未读取到数据尝试重新读取");
        }

    }

    /**
     * 加上存储数据库
     */
    private void saveTempTable() {
        Long beginTime = System.currentTimeMillis();
        mobileTempService.saveBatch(cachedDataList);
        log.info("{}保存数据耗时{}ms", guild, System.currentTimeMillis() - beginTime);
    }
}