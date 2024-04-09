package com.qxy.notice.domain;


public class NoticeData {
    private String billNo;

    private String content;

    private String userName;

    private String userNo;

    private String surplusTime;

    public NoticeData(String billNo, String content, String userName, String userNo, String surplusTime) {
        this.billNo = billNo;
        this.content = content;
        this.userName = userName;
        this.userNo = userNo;
        this.surplusTime = surplusTime;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(String surplusTime) {
        this.surplusTime = surplusTime;
    }
}
