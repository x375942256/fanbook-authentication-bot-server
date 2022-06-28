package com.example.demo.enums;

/**
 * 响应信息枚举类
 *
 * @Author 明辉
 * @Date 2021-06-09
 */
public enum ResponseEnum {

    SUCCESS(200, "操作成功"),

    USER_NO_OAUTH(100100, "用户未授权"),
    mobile_find(0,"已找到匹配的手机号码"),
    mobile_no_find(1,"手机号码未找到"),
    SAVE_USER_INFO_FAIL(100120, "存储用户信息已存在"),

    QUERY_USER_INFO_FAIL(100130, "查询用户信息失败"),

    SAVE_GAME_SCORE_FAIL(100140, "保存用户游戏得分失败"),
    SAVE_CONFIG_FAIL(10090, "保存失败"),
    GET_TOEKN_FAIL(10099, "授权失败"),
    QUERY_TOTAL_SCORE_RANK_FAIL(100150, "查询历史得分总榜失败"),

    QUERY_TODAY_SCORE_RANK_FAIL(100160, "查询今日得分总榜失败"),

    QUERY_TOTAL_AND_TODAY_FIRST_SCORE_FAIL(100170, "查询历史和今日得分第一名失败"),

    CARD_MESSAGE_SEND_FAIL(100180, "消息发送失败");

    private Integer code;
    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ResponseEnum() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
