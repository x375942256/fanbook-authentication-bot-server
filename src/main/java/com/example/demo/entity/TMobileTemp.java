package com.example.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (TMobileTemp)表实体类
 *
 * @author makejava
 * @since 2022-04-16 12:22:29
 */
@SuppressWarnings("serial")
@Data
public class TMobileTemp extends Model<TMobileTemp> {
    @TableId(type = IdType.AUTO)
    private Long id;
    //手机号码
    @ExcelProperty(index = 0)
    private String mobile;
    //服务器
    private Long guild;
    //角色名称
    @ExcelProperty(index = 1)
    private String roleName;
    //角色ID
    private String roleIds;

    private Integer sheetNo;
    @TableField(exist = false)
    @ExcelProperty(index = 2)
    private String remarks;

}
