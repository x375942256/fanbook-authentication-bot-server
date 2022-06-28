package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 消息卡片DTO
 *
 * @Author 明辉
 * @Date 2021-06-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageCardDTO {

    //用户ID
    private String userId;

    //游戏得分
    private Long score;

    //频道ID
    private String chatId;

    //频道ID
    private String guildname;
    private String gameName;
    private String botToken;

}
