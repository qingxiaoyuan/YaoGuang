package com.qxy.quartz.task;

import com.qxy.notice.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 定时任务调度
 */
@Component("yaoguangTask")
public class YaoGuangTask {

    @Autowired
    private INoticeService noticeServer;

    public void noticeDailyReport() throws IOException {
        noticeServer.noticeDailyReport();
    }

    public void noticeDevReport() {
        noticeServer.noticeForDev();
    }

    public void noticePMReport() {
        noticeServer.noticeForPM();
    }
}
