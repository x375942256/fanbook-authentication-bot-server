package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (TRecord)表实体类
 *
 * @author makejava
 * @since 2022-04-13 10:46:12
 */
@SuppressWarnings("serial")
@Data
public class TRecord extends Model<TRecord> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //fanbook用户ID
    private Long fbUser;
    //手机号码
    private String mobile;
    //服务器ID
    private Long guild;
    //状态
    private Integer status;

    private String createTime;
    private String updateTime;


}
