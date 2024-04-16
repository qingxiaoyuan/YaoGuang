package com.qxy.notice.domain;


public class NoticeData {
    private String billNo;

    private String content;

    private String currentUser;

    private String userNo;

    private int ayyyy;

    private String feedbackTime;

    public NoticeData(String billNo, String content, String currentUser, String userNo, int ayyyy, String feedbackTime) {
        this.billNo = billNo;
        this.content = content;
        this.currentUser = currentUser;
        this.userNo = userNo;
        this.ayyyy = ayyyy;
        this.feedbackTime = feedbackTime;
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


    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }


    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public int getAyyyy() {
        return ayyyy;
    }

    public void setAyyyy(int ayyyy) {
        this.ayyyy = ayyyy;
    }
}
