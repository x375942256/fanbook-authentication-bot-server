package com.example.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.TMobile;
import com.example.demo.entity.TRecord;
import com.example.demo.entity.Template;
import com.example.demo.service.FanBookService;
import com.example.demo.service.TMobileService;
import com.example.demo.service.TRecordService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExcelListener implements ReadListener<Template> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     *
     *
     */
    private static final int BATCH_COUNT = 1000;
    /**
     * 缓存的数据
     */
    private List<Template> cachedDataList = new ArrayList<>();
    FanBookService fanBookService;
    TMobileService tMobileService;
    TRecordService recordService;
    Long guild;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */

    public ExcelListener(Long guild, TMobileService tMobileService, FanBookService fanBookService, TRecordService recordService) {
        this.guild = guild;
        this.tMobileService = tMobileService;
        this.fanBookService = fanBookService;
        this.recordService = recordService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Template data, AnalysisContext context) {
        if (null != data && null != data.getMobile()) {
            cachedDataList.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (cachedDataList.size() >= BATCH_COUNT) {
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
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        Long beginTime = System.currentTimeMillis();
        log.info("{}处理第{}个sheet", guild, context.readSheetHolder().getSheetNo() + 1);
        if (cachedDataList.size() > 0) {

            List<TMobile> addList = new ArrayList<>();
            //处理要添加的手机号码
            cachedDataList.forEach(obj -> {
                List<TMobile> ts = tMobileService.list(Wrappers.<TMobile>lambdaQuery().eq(TMobile::getGuild, guild).eq(TMobile::getMobile, obj.getMobile()));
                if (ts.size() == 0) {
                    TMobile m = new TMobile();
                    m.setGuild(guild);
                    m.setRoleName(obj.getRoleName());
                    m.setMobile(obj.getMobile());
                    m.setSheetNo(context.readSheetHolder().getSheetNo());
                    addList.add(m);
                } else if (null != obj.getRoleName() && !obj.getRoleName().equals(ts.get(0).getRoleName())) {
                    tMobileService.update(Wrappers.<TMobile>lambdaUpdate().set(TMobile::getRoleName, obj.getRoleName()).eq(TMobile::getGuild, guild).eq(TMobile::getMobile, obj.getMobile()));
                }
            });
            //批量添加需要添加的信息
            tMobileService.saveBatch(addList);
            List<String> tempList = cachedDataList.stream().map(Template::getMobile).collect(Collectors.toList());
            //删除不存在文档中的账号
            List<TMobile> tMobiles = tMobileService.list(Wrappers.<TMobile>lambdaQuery().eq(TMobile::getGuild, guild).eq(TMobile::getSheetNo, context.readSheetHolder().getSheetNo()));
            tMobiles.forEach(tMobile -> {
                if (!tempList.contains(tMobile.getMobile())) {
                    //删除手机号码
                    tMobileService.removeById(tMobile.getId());
                    //删除赋予的角色
                    TRecord tRecord = recordService.getOne(Wrappers.<TRecord>lambdaQuery().eq(TRecord::getGuild, guild).eq(TRecord::getMobile, tMobile.getMobile()));
                    if (null != tRecord) {
                        log.info("删除用户：手机号码：{}||USER_ID:{}",tMobile.getMobile(),tRecord.getFbUser());
                        fanBookService.delMemberRoles(guild, tRecord.getFbUser(), guild.toString());

                    }
                }
            });
        }
        log.info("处理{}第{}个sheet耗时{}ms", guild, context.readSheetHolder().getSheetNo() + 1, System.currentTimeMillis() - beginTime);
    }

    /**
     * 加上存储数据库
     */
    private void saveTempTable() {

    }
}