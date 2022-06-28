package com.example.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Template {
    @ExcelProperty(index = 0)
    String mobile;
    @ExcelProperty(index = 1)
    String roleName;
//    @ExcelProperty(index = 2)
    String remarks;
}
