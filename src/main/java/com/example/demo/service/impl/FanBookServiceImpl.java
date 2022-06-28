package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.GuildRole;
import com.example.demo.service.FanBookService;
import com.example.demo.util.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FanBookServiceImpl implements FanBookService {


//    @Value("${fb.guild}")
//    Long guild;
//    @Value("${fb.roles}")
//    String roles;

    @Value("${fb.open.api.setMemberRoles}")
    String setMemberRolesUrl;
    @Value("${fb.open.api.getChatMember}")
    String getChatMemberUrl;
    @Value("${fb.open.api.getGuildRoles}")
    String getGuildRolesUrl;

    @Autowired
    FanBookService fanBookService;

    @Override
    public JSONObject setMemberRoles(Long userId) {
//        List<Long> roleslist = this.getUserRoles(guild, userId);
//        String[] strs = roles.split(",");
//        Arrays.asList(strs).forEach(obj ->{
//            roleslist.add(Long.valueOf(obj));
//        });
////        long[] ints = Arrays.stream(strs).mapToLong(Long::valueOf).toArray();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("guild_id", guild);
//        jsonObject.put("user_id", userId);
//        jsonObject.put("roles", roleslist);
//        log.debug(setMemberRolesUrl);
//        log.debug(jsonObject.toJSONString());
//        String s = RestTemplateUtil.builder().post(setMemberRolesUrl, jsonObject);
//        log.debug(s);
        return null;
    }

    @Override
    public JSONObject setMemberRoles(Long guild, Long userId, String roles) {
        List<Long> roleslist = this.getUserRoles(guild, userId);
        String replaceRoles = roles.replace("，", ",");
        String[] strs = replaceRoles.split(",");
        Arrays.asList(strs).forEach(obj -> {
            roleslist.add(Long.valueOf(obj));
        });
        // 获取服务器所有角色
        JSONArray arr = fanBookService.getGuildRoles(guild);
        List<GuildRole> guildRoles = JSONArray.parseArray(JSON.toJSONString(arr), GuildRole.class);
        List<Long> rolesIds = guildRoles.stream().map(i -> i.getId()).collect(Collectors.toList());
        // 过滤掉 不存在的角色
        List<Long> updateRoles = roleslist.stream().filter(role -> {
            return rolesIds.contains(role);
        }).collect(Collectors.toList());
//      long[] ints = Arrays.stream(strs).mapToLong(Long::valueOf).toArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("guild_id", guild);
        jsonObject.put("user_id", userId);
        jsonObject.put("roles", updateRoles);
        log.debug(setMemberRolesUrl);
        log.debug(userId + "设置角色参数：" + jsonObject.toJSONString());
        String s = RestTemplateUtil.builder().post(setMemberRolesUrl, jsonObject);
        log.debug(userId + "设置角色参数返回：" + s);
        return null;
    }

    @Override
    public JSONObject delMemberRoles(Long guild, Long userId, String roles) {
        List<Long> roleslist = this.getUserRoles(guild, userId);
        if(roles==null) {
            return null;
        }
        String[] strs = roles.split(",");
        for (String str:strs)
        {
            roleslist.remove(Long.valueOf(str));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("guild_id", guild);
        jsonObject.put("user_id", userId);
        jsonObject.put("roles", roleslist);
        log.debug(setMemberRolesUrl);
        log.debug(jsonObject.toJSONString());
        String s = RestTemplateUtil.builder().post(setMemberRolesUrl, jsonObject);
        log.debug(s);
        return null;
    }

    //获取聊天成员信息
    public String getChatMember(Long guild_id, Long user_id) {
        JSONObject pJson = new JSONObject();
        pJson.put("guild_id", guild_id);
        pJson.put("user_id", user_id);
        String str = RestTemplateUtil.builder().post(getChatMemberUrl, pJson);
        return str;
    }

    @Override
    public JSONArray getGuildRoles(Long guild) {
        JSONObject pJson = new JSONObject();
        pJson.put("guild_id", guild);
        String str = RestTemplateUtil.builder().post(getGuildRolesUrl, pJson);
        if (JSONObject.parseObject(str).getBoolean("ok")) {
            return JSONObject.parseObject(str).getJSONArray("result");
        }
        return null;
    }

    public List<Long> getUserRoles(Long guild_id, Long userId) {
        String strChatMember = this.getChatMember(guild_id, userId);
        List<Long> l = new ArrayList<>();
        if (null != strChatMember) {
            JSONArray jsonArray = JSONObject.parseObject(strChatMember).getJSONObject("result").getJSONArray("roles");
            if (null != jsonArray) {
                jsonArray.forEach(o1 -> {
                    if (o1 instanceof JSONObject) {
                        l.add(((JSONObject) o1).getLong("id"));
                    }
                });
            }
        }
        return l;
    }
}
