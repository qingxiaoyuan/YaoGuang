package com.qxy.notice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.qxy.common.constant.RobotConstants;
import com.qxy.common.utils.http.HttpUtils;
import com.qxy.notice.domain.EnterpriseRobotMessage;
import com.qxy.notice.domain.NoticeData;
import com.qxy.notice.service.INoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NoticeServiceImpl implements INoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Override
    public void noticeForDev() {
        String getNoticeForDevResult = HttpUtils.sendGet("https://gateway.ns820.com/v1/notification/reminder/getNoticeForDev");
        if (getNoticeForDevResult.isEmpty()) {
            return;
        }
        JSONObject noticeListObject = JSON.parseObject(getNoticeForDevResult);
        if (noticeListObject.getIntValue("errCode") == 0 && !noticeListObject.getJSONArray("data").isEmpty()) {
            try {
                List<NoticeData> noticeData = noticeListObject.getJSONArray("data").toJavaList(NoticeData.class);
                noticeData = noticeData.stream().filter(item -> !"220508".equals(item.getUserNo())).toList();
                List<String> noticeIds = noticeData.stream().map(NoticeData::getUserNo).toList();
                EnterpriseRobotMessage message = EnterpriseRobotMessage.textBuilder("今日开发超时问题单" + noticeIds.size() + "条，请相关同事注意.").addUserIdForAt(noticeIds.toArray(String[]::new)).build();
                HttpUtils.sendPost(RobotConstants.BOT_URL, message);
            } catch (JSONException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void noticeForPM() {
        String getNoticeForDevResult = HttpUtils.sendGet("https://gateway.ns820.com/v1/notification/reminder/getNoticeForPM");
        if (getNoticeForDevResult.isEmpty()) {
            return;
        }
        JSONObject noticeListObject = JSON.parseObject(getNoticeForDevResult);
        if (noticeListObject.getIntValue("errCode") == 0 && !noticeListObject.getJSONArray("data").isEmpty()) {
            try {
                List<NoticeData> noticeData = noticeListObject.getJSONArray("data").toJavaList(NoticeData.class);
                noticeData = noticeData.stream().filter(item -> !"220508".equals(item.getUserNo())).toList();
                List<String> noticeIds = noticeData.stream().map(NoticeData::getUserNo).toList();
                EnterpriseRobotMessage message = EnterpriseRobotMessage.textBuilder("今日开发超时问题单" + noticeIds.size() + "条，请相关同事注意.").addUserIdForAt(noticeIds.toArray(String[]::new)).build();
                HttpUtils.sendPost(RobotConstants.BOT_URL, message);
            } catch (JSONException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
