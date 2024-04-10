package com.qxy.notice.domain;

public class DailyReportData {

    private String PhId;

    private String Creator;

    private String Creator_EXName;

    public DailyReportData() {
    }

    public DailyReportData(String phId, String creator, String creator_EXName) {
        PhId = phId;
        Creator = creator;
        Creator_EXName = creator_EXName;
    }

    public String getPhId() {
        return PhId;
    }

    public void setPhId(String phId) {
        PhId = phId;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getCreator_EXName() {
        return Creator_EXName;
    }

    public void setCreator_EXName(String creator_EXName) {
        Creator_EXName = creator_EXName;
    }
}
