package com.example.demo.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Role;
import com.example.demo.entity.TMobile;
import com.example.demo.entity.TWord;
import com.example.demo.service.AsynService;
import com.example.demo.service.FanBookService;
import com.example.demo.service.TMobileService;
import com.example.demo.service.TxDocService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * @author : carson
 **/
@Slf4j
@Service
public class AsynServiceImpl implements AsynService {
    @Autowired
    TxDocService txDocService;

    @Autowired
    TMobileService tMobileService;

    @Autowired
    FanBookService fanBookService;

    /**
     * 导出腾讯文档
     *
     * @param work
     * @throws InterruptedException
     * @throws IOException
     */
    @Async
    @Override
    public void export(TWord work) throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis();
        JSONObject filsJson = txDocService.converter(work.getWorkCode());
        if (null == filsJson) {
            //失败尝试重试3次
            Integer num = 1;
            while (num < 5) {
                Thread.sleep(500);
                log.debug("转换文档ID接口异常尝试第{}次", num);
                filsJson = txDocService.converter(work.getWorkCode());
                if (null != filsJson) {
                    break;
                }
                num++;
            }
        }
        log.debug(filsJson.toJSONString());
        if (null == filsJson) {
            return;
        }
        String fileID = filsJson.getJSONObject("data").getString("fileID");
        JSONObject aaJson = txDocService.export(fileID);
        if (null == aaJson) {
            //失败尝试重试3次
            Integer num = 1;
            while (num < 5) {
                Thread.sleep(500);
                log.debug("导出文档接口异常尝试第{}次", num);
                aaJson = txDocService.export(fileID);
                if (null != aaJson) {
                    break;
                }
                num++;
            }
        }
        if (null != aaJson) {
            String operationID = aaJson.getJSONObject("data").getString("operationID");
            Integer progress = 0;
            String fileUrl = "";
            while (progress < 100) {
                JSONObject xJson = txDocService.progress(fileID, operationID);
                if (null != xJson) {
                    //每隔500ms获取文件进度
                    Thread.sleep(500);
                    progress = xJson.getJSONObject("data").getInteger("progress");
                    if (progress == 100) {
                        fileUrl = xJson.getJSONObject("data").getString("url");
                    }
                }

            }
            long x = System.currentTimeMillis() - startTime;
            log.debug("接口耗时：{}ms", x);
            if (!StringUtils.isEmpty(fileUrl)) {
                // 拉取数据
                txDocService.repeatedRead(fileUrl, work.getGuild());
                // 删除多余页的数据
                URL url = new URL(fileUrl);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(inStream);
                ExcelReader excelReader = excelReaderBuilder.build();
                List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
                log.debug("工作表页数:{}", sheets.size());
                // 删除掉多余页数的数据
                Integer size = sheets.size();
                this.tMobileService.deleteBySheet(work.getGuild(), size);
                this.update(work.getGuild());
            }
        }

    }

    @Override
    public void update(Long guild) {
        long startTime = System.currentTimeMillis();
        log.debug("开始更新角色ID");
        // 获取 服务器下 所有的角色
        JSONArray arr = fanBookService.getGuildRoles(guild);
        // 获取所有角色分组
        List<Role> roleGroup = tMobileService.getRoleGroup();
        roleGroup.forEach(role -> {
            if (null != role) {
                log.info(JSON.toJSONString(role));
                StringBuffer stringBuffer = new StringBuffer();
                if (!StringUtils.isEmpty(role.getRoleName())) {
                    String replaceRole = role.getRoleName().replaceAll("，", ",");
                    String[] roles = replaceRole.split(",");
                    if (roles.length > 0) {
                        Arrays.stream(roles).forEach(str ->
                        {
                            if (null != arr) {
                                arr.forEach(obj -> {
                                    JSONObject roleJson = JSONObject.parseObject(obj.toString());
                                    if (roleJson.getString("name").equals(str)) {
                                        stringBuffer.append(roleJson.getLong("id"));
                                        stringBuffer.append(",");
                                    }
                                });
                            }
                        });
                    }
                }
                if (stringBuffer.length() > 1) {
                    // 获取该组角色下的 所有用户
                    List<TMobile> tMobileList = this.tMobileService.getTMobileList(role.getRoleName());
                    String rolesIds = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
                    tMobileList.stream().forEach(tMobile -> {
                        tMobile.setRoleIds(rolesIds);
                    });
                    // 批量更新
                    tMobileService.updateBatchById(tMobileList);
                }
            }

        });
        long x = System.currentTimeMillis();
        log.debug("更新角色结束，耗时:{} ms", x - startTime);
    }


}
