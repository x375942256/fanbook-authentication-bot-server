package com.example.demo.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.TConfig;
import com.example.demo.entity.TMobileTemp;
import com.example.demo.entity.Template;
import com.example.demo.listener.ExcelListener2;
import com.example.demo.service.*;
import com.example.demo.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TxDocServiceImpl implements TxDocService {
    @Value("${tx.doc.api.oauth.token}")
    String tokenUrl;
    @Value("${tx.doc.api.export.converter}")
    String converterUrl;
    @Value("${tx.doc.api.export.async}")
    String asyncUrl;
    @Value("${tx.doc.api.export.progress}")
    String progressUrl;

    @Autowired
    TFromService fromService;
    @Autowired
    TConfigService configService;
    @Autowired
    TMobileService tMobileService;
    @Autowired
    TMobileTempService tMobileTempService;
    @Autowired
    FanBookService fanBookService;
    @Autowired
    TRecordService recordService;

    @Override
    public JSONObject converter() {
        log.debug("转换文档ID");
//        String url = converterUrl.concat("?type=2&value=DSXdSbHpTamRnb1hp");
        TConfig config = configService.getById(1);
        ;
        String str = OkHttpUtils
                .builder().
                        url(converterUrl)
                .addHeader("Access-Token", config.getTxUserToken())
                .addHeader("Client-Id", config.getTxClientid())
                .addHeader("Open-Id", config.getTxUserOpenid())
                .addParam("type", "2")
                .addParam("value", config.getWordCode())
                .get()
                .async();
        return JSONObject.parseObject(str);
    }

    @Override
    public JSONObject converter(String workCode) {
//        log.debug("转换文档ID");
//        String url = converterUrl.concat("?type=2&value=DSXdSbHpTamRnb1hp");
        TConfig config = configService.getById(1);

        String str = OkHttpUtils
                .builder().
                        url(converterUrl)
                .addHeader("Access-Token", config.getTxUserToken())
                .addHeader("Client-Id", config.getTxClientid())
                .addHeader("Open-Id", config.getTxUserOpenid())
                .addParam("type", "2")
                .addParam("value", workCode)
                .get()
                .async();
        return JSONObject.parseObject(str);
    }

    @Override
    public JSONObject export(String fileID) {
        TConfig config = configService.getById(1);
//        log.debug("开始导出文档");
        String str = OkHttpUtils
                .builder().
                        url(asyncUrl.replace("fileID", fileID))
                .addHeader("Access-Token", config.getTxUserToken())
                .addHeader("Client-Id", config.getTxClientid())
                .addHeader("Open-Id", config.getTxUserOpenid())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addParam("exportType", "sheet")
                .post(false)
                .async();
        return JSONObject.parseObject(str);
    }

    @Override
    public JSONObject progress(String fileID, String operationID) {
//        log.debug("查询导出进度");
        TConfig config = configService.getById(1);
        String str = OkHttpUtils
                .builder().
                        url(progressUrl.replace("fileID", fileID))
                .addHeader("Access-Token", config.getTxUserToken())
                .addHeader("Client-Id", config.getTxClientid())
                .addHeader("Open-Id", config.getTxUserOpenid())
                .addParam("operationID", operationID)
                .get()
                .async();
        return JSONObject.parseObject(str);
    }

    @Override
    public JSONObject refreshToken() {
        TConfig config = configService.getById(1);
        String str = OkHttpUtils.builder()
                .url(tokenUrl)
                .addParam("client_id", config.getTxClientid())
                .addParam("client_secret", config.getTxSecret())
                .addParam("grant_type", "refresh_token")
                .addParam("refresh_token", config.getTxUserRefreshToken())
                .get()
                .sync();
        JSONObject json = JSONObject.parseObject(str);
        if (null != json && json.containsKey("access_token")) {
            UpdateWrapper<TConfig> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", 1);
            updateWrapper.set("tx_user_token", json.getString("access_token"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //从前端或者自己模拟一个日期格式，转为String即可
            String dateStr = format.format(new Date());
            updateWrapper.set("update_time", dateStr);
            configService.update(updateWrapper);
        }
        return null;
    }

    @Override
    public void readExcl(String fileUrl) {
//        log.debug("读取文件");
//        Long beginTime = System.currentTimeMillis();
//        try {
//            URL url = new URL(fileUrl);
//            URLConnection conn = url.openConnection();
//            InputStream inStream = conn.getInputStream();
//            List<Object> list = EasyExcel.read(inStream, new Sheet(1));
//            String listString = JSONObject.toJSONString(list);
//            JSONArray arryList = JSONObject.parseArray(listString);
//            log.info("arryList:{}", arryList);
//            // 处理数据
//            List<TFrom> listMap = new ArrayList<>();
//            Set<String> mobileList = new HashSet<>();
//            TFrom from = null;
//            for (Object obj : arryList) {
//
//                if (null != obj) {
//                    JSONArray rowData = JSONObject.parseArray(JSONObject.toJSONString(obj));
//                    mobileList.add(rowData.get(0).toString());
//                }
//            }
//            if (mobileList.size() == 0) {
//                return;
//            }
//            List<TFrom> tFroms = fromService.list();
//            List<TFrom> addList = new ArrayList<>();
//            //处理要添加的手机号码
//            mobileList.forEach(obj -> {
//                TFrom t = fromService.getOne(Wrappers.<TFrom>lambdaQuery().eq(TFrom::getMobile, obj));
//                if (null == t) {
//                    addList.add(TFrom.builder().mobile(obj).build());
//                }
//            });
//            fromService.saveBatch(addList);
//            //删除不存在文档中的账号
//            tFroms.forEach(tFrom -> {
//                if (!mobileList.contains(tFrom.getMobile())) {
//                    fromService.removeById(tFrom.getId());
//                }
//            });
//            log.info("data:{}", JSONObject.toJSONString(listMap));
//            Long time = System.currentTimeMillis() - beginTime;
//            log.info("处理文件耗时：" + time + "ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void readExcl(String fileUrl, Long guild) {
//        log.debug("读取文件");
//        Long beginTime = System.currentTimeMillis();
//        try {
//            URL url = new URL(fileUrl);
//            URLConnection conn = url.openConnection();
//            InputStream inStream = conn.getInputStream();
//            List<Object> list = EasyExcel.read(inStream, new Sheet(1));
//            String listString = JSONObject.toJSONString(list);
//            JSONArray arryList = JSONObject.parseArray(listString);
//            log.info("arryList:{}", arryList);
//            // 处理数据
//            List<TFrom> listMap = new ArrayList<>();
//            Set<String> mobileList = new HashSet<>();
//
//            for (int i = 1; i < arryList.size(); i++) {
//                if (null != arryList.get(i)) {
//                    JSONArray rowData = JSONObject.parseArray(JSONObject.toJSONString(arryList.get(i)));
//                    mobileList.add(rowData.get(0).toString());
//                }
//            }
//            if (mobileList.size() == 0) {
//                return;
//            }
//            List<TFrom> tFroms = fromService.list();
//
//            QueryWrapper<TMobile> queryMobileWrapper = new QueryWrapper<>();
//            queryMobileWrapper.eq("guild", guild);
//            List<TMobile> tMobiles = tMobileService.list(queryMobileWrapper);
//            List<TMobile> addList = new ArrayList<>();
////            List<TFrom> addList = new ArrayList<>();
//
//            //处理要添加的手机号码
//            mobileList.forEach(obj -> {
//                TMobile t = tMobileService.getOne(Wrappers.<TMobile>lambdaQuery().eq(TMobile::getGuild, guild).eq(TMobile::getMobile, obj));
//                if (null == t) {
//                    addList.add(TMobile.builder().guild(guild).mobile(obj).build());
//                }
//            });
//            //批量添加需要添加的信息
//            tMobileService.saveBatch(addList);
//            //删除不存在文档中的账号
//            tMobiles.forEach(tMobile -> {
//                if (!mobileList.contains(tMobile.getMobile())) {
//                    //删除手机号码
//                    tMobileService.remove(Wrappers.<TMobile>query().lambda().eq(TMobile::getGuild, guild).eq(TMobile::getMobile, tMobile.getMobile()));
//                    //删除赋予的角色
////                    fanBookService.delMemberRoles(guild,)
//                }
//            });
//            log.info("data:{}", JSONObject.toJSONString(listMap));
//            Long time = System.currentTimeMillis() - beginTime;
//            log.info("处理文件耗时：" + time + "ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public Object readExcel(String url, Long guild) {


        return null;
    }

    @Override
    public List<Template> repeatedRead(String fileUrl, Long guild) {
        try {

            Long startTime=System.currentTimeMillis();
            log.debug("读取{}服务器文档地址{}",guild,fileUrl);
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            // 读取全部sheet
            //清楚临时表中需要拉取的数据信息
            tMobileTempService.remove(Wrappers.<TMobileTemp>lambdaUpdate().eq(TMobileTemp::getGuild, guild));
            EasyExcel.read(inStream, TMobileTemp.class, new ExcelListener2(guild, tMobileService, fanBookService, recordService,tMobileTempService)).doReadAll();

            long x = System.currentTimeMillis() - startTime;
            log.debug("读取{}服务器文档耗时{}ms",guild,x);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
