package com.haivera.processcenter.common.util;
import com.alibaba.fastjson.JSONObject;

public class ResponseInfo<T> {
    //状态码
    private String rtnCode;
    //状态信息
    private String rtnMsg;
    //数据对象
    private T data;

    public ResponseInfo() {
    }

    public ResponseInfo(String rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
    }

    public ResponseInfo(String rtnCode, String rtnMsg, T data) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
        this.data = data;
    }

    public ResponseInfo(String rtnCode, T data) {
        this.rtnCode = rtnCode;
        this.rtnMsg = ResponseUtil.getMsgByCode(rtnCode);
        this.data = data;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void doSuccess(String rtnMsg){
        this.rtnCode = ResponseUtil.SUCCESS_CODE;
        this.rtnMsg = rtnMsg;
    }

    public void doSuccess(String rtnMsg, T data){
        this.rtnCode = ResponseUtil.SUCCESS_CODE;
        this.rtnMsg = rtnMsg;
        this.data = data;
    }

    public void doFailed(String rtnMsg){
        this.rtnCode = ResponseUtil.FAILED_CODE;
        this.rtnMsg = rtnMsg;
    }

    public void doFailed(String rtnMsg, T data){
        this.rtnCode = ResponseUtil.FAILED_CODE;
        this.rtnMsg = rtnMsg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseInfo [rtnCode=" + rtnCode + ", rtnMsg=" + rtnMsg + ", data=" + data +"]";
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }
}
