package com.qxy.notice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.qxy.common.constant.CacheConstants;
import com.qxy.common.constant.RobotConfig;
import com.qxy.common.core.domain.ResponseEntity;
import com.qxy.common.core.domain.entity.SysUser;
import com.qxy.common.core.redis.RedisCache;
import com.qxy.common.utils.common.DateUtils;
import com.qxy.common.utils.http.HttpUtils;
import com.qxy.notice.domain.DailyReportData;
import com.qxy.notice.domain.EnterpriseRobotMessage;
import com.qxy.notice.domain.NoticeData;
import com.qxy.notice.service.INoticeService;
import com.qxy.system.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NoticeServiceImpl implements INoticeService {

    private static final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RobotConfig robotConfig;

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
                HttpUtils.sendPost(robotConfig.getBotUrl(), message);
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
                HttpUtils.sendPost(robotConfig.getBotUrl(), message);
            } catch (JSONException e) {
                log.error(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ResponseEntity getDailyReportDate(LocalDate preDate, String dailySystemToken) throws IOException {
        HashMap<String, String> queryData = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        HashMap<String, String> header = new HashMap<>();
        queryData.put("FillDt*date*ge*1", preDate.toString());
        queryData.put("FillDt*date*lt*1", preDate.plusDays(1).toString());
        params.put("page", "0");
        params.put("start", "0");
        params.put("limit", "1000");
        params.put("queryfilter", JSON.toJSONString(queryData));
        header.put("Authorization", dailySystemToken);
        return HttpUtils.sendPost(robotConfig.getReportServerUrl() + "/PMS/RDM/TaskFill/GetTaskFillList", params, header);
    }

    private List<String> getNeedNoticeList(List<DailyReportData> dailyReportData, List<SysUser> userList) {
        List<String> list = new ArrayList<>();
        userList.forEach(user -> {
            boolean findData = dailyReportData.stream().anyMatch(data -> data.getCreator_EXName().equals(user.getNickName()));
            if (!findData) {
                list.add(user.getUserName());
            }
        });
        return list;
    }

    @Override
    public void noticeDailyReport() throws IOException {
        LocalDate currentDate = LocalDate.now();
        LocalDate preDate = DateUtils.getLastWorkday(currentDate);
        List<SysUser> userList = userMapper.selectUserList(new SysUser());
        String dailyTokenKey = CacheConstants.THIRD_SYSTEM_TOKEN_KEY + "daily_token";
        String dailySystemToken;
        if (redisCache.hasKey(dailyTokenKey)) {
            dailySystemToken = redisCache.getCacheObject(dailyTokenKey);
        } else {
            String getTokenResult = HttpUtils.sendGet(robotConfig.getReportServerUrl() +
                    "/api/kernelsession?loginid=" + robotConfig.getObserverId() +
                    "&ucode" + robotConfig.getObserverCode() + "&devicename=" +
                    robotConfig.getObserverId() + "_" + robotConfig.getObserverCode());
            JSONObject tokenObject = JSON.parseObject(getTokenResult);
            dailySystemToken = tokenObject.getString("accesstoken");
            redisCache.setCacheObject(dailyTokenKey, dailySystemToken);
        }
        ResponseEntity responseEntity = getDailyReportDate(preDate, dailySystemToken);
        List<String> needNoticePerson = null;
        // 401一次说明本次token过期，再刷一次token重试
        if (responseEntity.getHttpStatusCode() == 401) {
            String getTokenResult = HttpUtils.sendGet(robotConfig.getReportServerUrl() +
                    "/api/kernelsession?loginid=" + robotConfig.getObserverId() +
                    "&ucode" + robotConfig.getObserverCode() + "&devicename=" +
                    robotConfig.getObserverId() + "_" + robotConfig.getObserverCode());
            JSONObject tokenObject = JSON.parseObject(getTokenResult);
            dailySystemToken = tokenObject.getString("accesstoken");
            redisCache.setCacheObject(dailyTokenKey, dailySystemToken);

            ResponseEntity reResponseEntity = getDailyReportDate(preDate, dailySystemToken);
            List<DailyReportData> resultList = JSON.parseObject(responseEntity.getMessage()).getJSONArray("Record").toJavaList(DailyReportData.class);
            needNoticePerson = getNeedNoticeList(resultList, userList);
        }
        if (responseEntity.getHttpStatusCode() == 200 && !responseEntity.getMessage().isEmpty()) {
            List<DailyReportData> resultList = JSON.parseObject(responseEntity.getMessage()).getJSONArray("Record").toJavaList(DailyReportData.class);
            needNoticePerson = getNeedNoticeList(resultList, userList);
        }
        if (!needNoticePerson.isEmpty()) {
            EnterpriseRobotMessage message = EnterpriseRobotMessage.textBuilder("青小瑶检测到被@的朋友未填写" + preDate + "的日报").addUserIdForAt(needNoticePerson.toArray(String[]::new)).build();
            HttpUtils.sendPost(robotConfig.getBotUrl(), message);
        }

    }
}
