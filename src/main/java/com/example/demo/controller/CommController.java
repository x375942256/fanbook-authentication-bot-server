package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.TWord;
import com.example.demo.enums.ResponseEnum;
import com.example.demo.model.ResponseResult;
import com.example.demo.service.AsynService;
import com.example.demo.service.TWordService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequestMapping("/word")
@RestController
@Slf4j
public class CommController {

    @Autowired
    TWordService wordService;
    @Autowired
    AsynService asynService;

    /**
     * 授权回调地址
     *
     * @return Msg
     */
    @ApiOperation("手动拉取文档数据")
    @ResponseBody
    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    public ResponseResult sync(HttpServletRequest request, @RequestParam("guild") String guild) {
        log.debug("服务器ID:" + guild);
        List<TWord> worklist = wordService.list(Wrappers.<TWord>lambdaQuery().eq(TWord::getGuild, guild));
        worklist.forEach(work -> {
            try {
                asynService.export(work);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                "");
    }

    /**
     * 授权回调地址
     *
     * @return Msg
     */
    @ApiOperation("添加文档配置")
    @ResponseBody
    @RequestMapping(value = "/addWord", method = RequestMethod.POST)
    public ResponseResult addWord(HttpServletRequest request, @RequestBody TWord tWord) {
        log.debug("添加文档参数:" + JSON.toJSONString(tWord));
        wordService.save(tWord);
        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                "");
    }
}
