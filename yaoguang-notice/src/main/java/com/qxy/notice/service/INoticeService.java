package com.qxy.notice.service;

import java.io.IOException;

public interface INoticeService {

    /**
     * 超时问题单提醒(开发)
     */
    void noticeForDev();

    /**
     * 超时问题单提醒(产品)
     */
    void noticeForPM();

    /**
     * 日报提醒
     */
    void noticeDailyReport() throws IOException;
}
