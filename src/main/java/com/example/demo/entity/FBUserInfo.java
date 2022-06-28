package com.example.demo.entity;

import lombok.Data;

@Data
public class FBUserInfo {
    Long user_id;
    String nickname;
    Long username;
    String avatar;
    UserToken token;
}
