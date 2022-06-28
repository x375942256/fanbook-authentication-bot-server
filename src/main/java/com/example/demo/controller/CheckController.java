package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.TFrom;
import com.example.demo.enums.ResponseEnum;
import com.example.demo.model.ResponseResult;
import com.example.demo.service.TFromService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/test")
@RestController
@Slf4j
public class CheckController {

    @Autowired
    TFromService fromService;

    /**
     * 授权回调地址
     *
     * @return Msg
     */
    @ApiOperation("检查手机号码")
    @ResponseBody
    @RequestMapping(value = "/checkMobile", method = RequestMethod.GET)
    public ResponseResult checkMobile(HttpServletRequest request, @RequestParam("mobile") String mobile) {
        log.debug("mobile:" + mobile);
        Integer count = fromService.count(Wrappers.<TFrom>lambdaQuery().eq(TFrom::getMobile, mobile));
        if (count > 0) {
            return new ResponseResult(
                    ResponseEnum.mobile_find.getCode(),
                    ResponseEnum.mobile_find.getMsg(),
                    "");
        } else {
            return new ResponseResult(
                    ResponseEnum.mobile_no_find.getCode(),
                    ResponseEnum.mobile_no_find.getMsg(),
                    "");
        }

    }
}
