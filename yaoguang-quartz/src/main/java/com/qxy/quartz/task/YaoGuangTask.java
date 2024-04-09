package com.qxy.quartz.task;

import com.qxy.notice.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度
 */
@Component("yaoguangTask")
public class YaoGuangTask {

    @Autowired
    private INoticeService noticeServer;

    public void noticeDailyReport() {
        System.out.println("执行无参方法");
    }

    public void noticeDevReport() {
        noticeServer.noticeForDev();
    }

    public void noticePMReport() {
        noticeServer.noticeForPM();
    }
}
