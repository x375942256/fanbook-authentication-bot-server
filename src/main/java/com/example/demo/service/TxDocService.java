package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Template;

import java.util.List;

public interface TxDocService {

    JSONObject converter();

    JSONObject converter(String wordCode);

    JSONObject export(String fileID);

    JSONObject progress(String fileID, String operationID);

    JSONObject refreshToken();

    void readExcl(String url);

    void readExcl(String url, Long guild);

    Object readExcel(String url, Long guild);

    //获取文档sheet列表
    List<Template> repeatedRead(String url, Long guild);
}
