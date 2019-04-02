package com.qf.api.sms.pojo;

import java.io.Serializable;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 * 封装了返回的信息
 */
public class SMSResponse implements Serializable{
    private String respCode;
    private String respDesc;
    private String failCount;
    private String[] failList;
    private String smsId;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }

    public String[] getFailList() {
        return failList;
    }

    public void setFailList(String[] failList) {
        this.failList = failList;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}
