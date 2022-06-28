package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.FBUserInfo;
import com.example.demo.entity.TMobile;
import com.example.demo.entity.TRecord;
import com.example.demo.enums.ResponseEnum;
import com.example.demo.model.ResponseResult;
import com.example.demo.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/fanbook")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FBController {

    @Value("${fb.open.fanbook_oauth_page}")
    String fanbookOauthPage;
    private final OauthService oauthService;
    private final TMobileService mobileService;
    private final FanBookService fanBookService;
    private final TxDocService txDocService;
    private final TRecordService recordService;

    /**
     * 授权回调地址
     *
     * @return Msg
     */
    @ApiOperation("登录授权接口")
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseResult login(HttpServletRequest request, @RequestParam("code") String code, @RequestParam("guild") Long guild) {
        log.debug("登录授权接口");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            code = URLDecoder.decode(code, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject tokenJson = oauthService.getToken(code);
        if (!tokenJson.containsKey("access_token")) {
            return new ResponseResult(
                    ResponseEnum.GET_TOEKN_FAIL.getCode(),
                    ResponseEnum.GET_TOEKN_FAIL.getMsg(),
                    "");
        }
        String access_token = tokenJson.getString("access_token");
        FBUserInfo user = oauthService.getMe(access_token);
        log.debug("当前认证用户：{}", user);
        String mobile = oauthService.getLink(access_token);
        List<TMobile> tMobileList = mobileService.list(Wrappers.<TMobile>lambdaQuery().eq(TMobile::getGuild, guild).eq(TMobile::getMobile, mobile));
        log.debug("验证手机是否存在：{}", user);
        if (null != tMobileList && tMobileList.size() > 0) {
            JSONObject json = new JSONObject();
            json.put("username", user.getNickname());
            //判读是否重复认证
            TRecord tr = recordService.getOne(Wrappers.<TRecord>lambdaQuery().eq(TRecord::getFbUser, user.getUser_id()).eq(TRecord::getGuild, guild).eq(TRecord::getMobile, tMobileList.get(0).getMobile()));
            if (null == tr) {
                //保存记录
                TRecord tRecord = new TRecord();
                tRecord.setGuild(guild);
                tRecord.setMobile(tMobileList.get(0).getMobile());
                tRecord.setFbUser(user.getUser_id());
                tRecord.setStatus(1);
                tRecord.setCreateTime(sdf.format(new Date()));
                recordService.save(tRecord);
                //设置角色
            } else {
                recordService.update(Wrappers.<TRecord>lambdaUpdate().eq(TRecord::getId, tr.getId()).set(TRecord::getUpdateTime, sdf.format(new Date())));
            }
            String roleIds = tMobileList.stream().filter(t -> t.getRoleIds() != null).map(TMobile::getRoleIds).collect(Collectors.joining(","));
            log.debug("roleIds:{}", roleIds);
            if (!StringUtils.isEmpty(roleIds)) {
                fanBookService.setMemberRoles(guild, user.getUser_id(), roleIds);
            }
//            tMobile.setMobile("");
            return new ResponseResult(
                    ResponseEnum.mobile_find.getCode(),
                    ResponseEnum.mobile_find.getMsg(),
                    json);
        } else {
            return new ResponseResult(
                    ResponseEnum.mobile_no_find.getCode(),
                    ResponseEnum.mobile_no_find.getMsg(),
                    "");
        }
    }

    /**
     * 重定向
     */
    @ApiOperation("登录重定向接口")
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void redirect(HttpServletResponse response) {
        log.debug("登录重定向接口");
        try {
            response.sendRedirect(fanbookOauthPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前登录者信息
     */
    @ApiOperation("获取用户信息接口")
    @ResponseBody
    @RequestMapping(value = "/getMe", method = RequestMethod.GET)
    public ResponseResult getMe(@RequestHeader("fbtoken") @Validated String token) {
//        Object tokenObj = request.getHeader("fbtoken");
        FBUserInfo user = oauthService.getMe(token);
        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                user);

    }

    /**
     * 测试接口
     */
    @ApiOperation("测试接口")
    @ResponseBody
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseResult ping() {
//        Object tokenObj = request.getHeader("fbtoken");
        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                "");

    }

    /**
     * 测试接口
     */
    @ApiOperation("刷新token")
    @ResponseBody
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    public ResponseResult refreshToken() {
//        Object tokenObj = request.getHeader("fbtoken");
        txDocService.refreshToken();
        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                "");

    }

//    /**
//     * 获取服务器列表
//     *
//     */
//    @ResponseBody
//    @RequestMapping(value = "/getGuilds", method = RequestMethod.GET)
//    public Result getGuilds(@RequestHeader("fbtoken") @Validated String token) {
////        Object tokenObj = request.getHeader("fbtoken");
//        JSONObject str = oauthService.getGuilds(token);
//        return Result.success(str);
//
//    }
}
