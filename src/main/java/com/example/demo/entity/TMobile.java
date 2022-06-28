package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


/**
 * (TMobile)表实体类
 *
 * @author makejava
 * @since 2022-04-11 20:43:40
 */
@Data
public class TMobile extends Model<TMobile> {
    @TableId(type = IdType.AUTO)
    private Long id;
    //手机号码
    private String mobile;
    //服务器
    private Long guild;
    //角色名称
    private String roleName;
    //角色ID
    private String roleIds;
    private Integer sheetNo;
}
