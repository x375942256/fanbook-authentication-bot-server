package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface FanBookService {
    JSONObject setMemberRoles(Long userId);

    JSONObject setMemberRoles(Long guild, Long userId, String roles);
    JSONObject delMemberRoles(Long guild, Long userId, String roles);

    JSONArray getGuildRoles(Long guild);
}
