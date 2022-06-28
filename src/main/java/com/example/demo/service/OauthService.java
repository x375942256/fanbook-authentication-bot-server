package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.FBUserInfo;
import com.example.demo.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class OauthService {
    @Value("${fb.open.api.getToken}")
    String apiTokenUrl;
    //    @Value("${fb.open.api.refresh_token}")
//    String apiRefresTokenUrl;
    @Value("${fb.open.api.getMe}")
    String apiGetMeUrl;
    //    @Value("${fb.open.api.getGuilds}")
//    String apiGetGuildsUrl;
    @Value("${fb.open.client}")
    String clientID;
    @Value("${fb.open.sceret}")
    String sceret;
    @Value("${fb.open.redirect_uri}")
    String redirectUri;
    @Value("${fb.open.api.link}")
    String linkUrl;

    public JSONObject getToken(String code) {
        String base64String = clientID + ":" + sceret;
        String str11 = Base64.getMimeEncoder().encodeToString(base64String.getBytes(StandardCharsets.UTF_8));
        String rJson = OkHttpUtils
                .builder().url(apiTokenUrl)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("authorization", "Basic " + str11)
                .addParam("grant_type", "authorization_code")
                .addParam("code", code)
                .addParam("redirect_uri", redirectUri)
                .post(false)
                .sync();
        log.debug("getToken");
        log.debug("code:" + code);
        log.debug("parameter:" + apiTokenUrl);
        log.debug("parameter:" + str11);
        log.debug("parameter:" + redirectUri);
        log.debug("rJson:" + rJson);
        return JSONObject.parseObject(rJson);
    }

    public FBUserInfo getMe(String access_token) {
        String rJson = OkHttpUtils
                .builder().url(apiGetMeUrl)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + access_token)
                .get()
                .sync();
        FBUserInfo userInfo = null;
        if (null != rJson) {
            JSONObject userJson = JSONObject.parseObject(rJson).getJSONObject("data");
            if (null != userJson) {
                userInfo = userJson.toJavaObject(FBUserInfo.class);
            }
        }
        return userInfo;
    }

    public String getLink(String access_token) {
        String rJson = OkHttpUtils
                .builder().url(linkUrl)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + access_token)
                .post(false)
                .sync();


        if (null != rJson) {
            JSONObject userJson = JSONObject.parseObject(rJson).getJSONObject("data");
            if (null != userJson) {
                String str = userJson.getString("mobile");
                byte[] bytes = Base64.getDecoder().decode(str);
                try {
                    return new String(bytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
//    public JSONObject getGuilds(String access_token) {
//        String rJson = OkHttpUtils
//                .builder().url(apiGetGuildsUrl)
//                .addHeader("content-type", "application/json")
//                .addHeader("authorization", "Bearer " + access_token)
//                .get()
//                .sync();
//        return JSONObject.parseObject(rJson);
//    }

}
