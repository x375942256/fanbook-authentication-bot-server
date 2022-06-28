package com.example.demo.entity;

import lombok.Data;

/**
 * @author haojia.guo
 * @create 2022年04月19日 14:07
 * @desc
 */
@Data
public class GuildRole {
    private Long id;
    private String name;
    private Long position;
    private Long permissions;
    private Long color;
}
