package com.example.demo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static String str = "HH:mm:ss";
    static SimpleDateFormat dateFormat = new SimpleDateFormat(str);

    public static void main(String[] args) {



    }


    private List<List<String>>  getExcelData(JSONArray arr){
        List<List<String>> data = new ArrayList<List<String>>();
//        for(int i=0;i<=10;i++){
//            List<String> line = new ArrayList<String>();
//            line.add("第"+i+"行，第1列内容");
//            data.add(line);
//        }
        arr.forEach(obj->{
            List<String> line = new ArrayList<String>();
            line.add(JSONObject.parseObject(obj.toString()).getJSONObject("user").getString("id"));
            line.add(JSONObject.parseObject(obj.toString()).getJSONObject("user").getString("first_name"));
            line.add(JSONObject.parseObject(obj.toString()).getJSONObject("user").getString("username"));
            data.add(line);
        });
        return data;
    }

}
