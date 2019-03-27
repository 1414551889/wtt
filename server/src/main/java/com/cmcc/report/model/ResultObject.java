package com.cmcc.report.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResultObject<T> implements Serializable {

	private static final long serialVersionUID = -1659325187297557458L;
	/**
     * @author HF
     * 0 统一表示成功
     * 1 统一表示失败
     * 其它数值根据自己的业务逻辑枚举
     */
    @JsonProperty("code")
    private long code;
    @JsonProperty("desc")
    private String desc;
    private String redirectUrl;
    @JsonProperty("data")
    private T object;
    private String token; // 重复提交令牌

    public ResultObject() {
    }

    public ResultObject(long code) {
        this.setCode(code);
    }

    public ResultObject(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public ResultObject(long code, String desc, T object) {
        this.code = code;
        this.desc = desc;
        this.object = object;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
        if(code==0) desc="成功";
        if(code==1) desc="失败";
        if(code==2) desc="用户未登陆";
    }
    public void setResult(long code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
