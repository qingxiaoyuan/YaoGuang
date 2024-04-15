package com.qxy.common.constant;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.stereotype.Component;

@Component
public class RobotConfig {

    /**
     * 机器人key码
     */
    @NacosValue(value = "${wechat.bot_url}", autoRefreshed = true)
    private String botUrl;

    /**
     * 观测日报系统的地址
     */
    @NacosValue(value = "${wechat.report_server_url}", autoRefreshed = true)
    private String reportServerUrl;

    /**
     * 观测者账号
     */
    @NacosValue(value = "${wechat.observer_id}", autoRefreshed = true)
    private String observerId;

    /**
     * 观测帐套
     */
    @NacosValue(value = "${wechat.observer_code}", autoRefreshed = true)
    private String observerCode;

    public String getBotUrl() {
        return botUrl;
    }

    public String getReportServerUrl() {
        return reportServerUrl;
    }

    public String getObserverId() {
        return observerId;
    }

    public String getObserverCode() {
        return observerCode;
    }
}
