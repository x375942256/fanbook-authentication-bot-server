package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * (TConfig)表实体类
 *
 * @author makejava
 * @since 2022-03-23 23:19:30
 */
@SuppressWarnings("serial")
public class TConfig extends Model<TConfig> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String txClientid;
    
    private String txSecret;
    
    private String txUserToken;
    
    private String txUserOpenid;
    
    private String txUserRefreshToken;
    //文档CODE
    private String wordCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTxClientid() {
        return txClientid;
    }

    public void setTxClientid(String txClientid) {
        this.txClientid = txClientid;
    }

    public String getTxSecret() {
        return txSecret;
    }

    public void setTxSecret(String txSecret) {
        this.txSecret = txSecret;
    }

    public String getTxUserToken() {
        return txUserToken;
    }

    public void setTxUserToken(String txUserToken) {
        this.txUserToken = txUserToken;
    }

    public String getTxUserOpenid() {
        return txUserOpenid;
    }

    public void setTxUserOpenid(String txUserOpenid) {
        this.txUserOpenid = txUserOpenid;
    }

    public String getTxUserRefreshToken() {
        return txUserRefreshToken;
    }

    public void setTxUserRefreshToken(String txUserRefreshToken) {
        this.txUserRefreshToken = txUserRefreshToken;
    }

    public String getWordCode() {
        return wordCode;
    }

    public void setWordCode(String wordCode) {
        this.wordCode = wordCode;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }

