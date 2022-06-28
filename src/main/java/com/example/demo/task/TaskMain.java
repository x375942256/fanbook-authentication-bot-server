package com.example.demo.task;

import com.example.demo.entity.TWord;
import com.example.demo.service.AsynService;
import com.example.demo.service.TMobileService;
import com.example.demo.service.TWordService;
import com.example.demo.service.TxDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TaskMain {
    @Autowired
    TxDocService txDocService;
    @Autowired
    AsynService asynService;
    @Autowired
    TWordService wordService;
    @Autowired
    TMobileService mobileService;

    //获取fanbook消息列表
//    @Scheduled(cron = "*/30 * * * * ?")
//    @Scheduled(cron = " 0 0/30 * * * ?")
    // 整点扫描一次
    @Scheduled(cron = "0 0 0/1 * * ?")
    @PostConstruct //启动项目先执行一次
    public void syncTask() {
        List<TWord> worklist = wordService.list();
        worklist.forEach(work -> {
            try {
                asynService.export(work);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //刷新token 20天执行一次
    @Scheduled(cron = "0 0 0 1/20 * ?")
    public void refreshToken() {
        txDocService.refreshToken();
    }

    // 每小时第8分钟执行一次
    @Scheduled(cron = "0 8 0/1 * * ?  ")
    public void clear() {
        // 清除重复的数据
        mobileService.clear();
    }
}
