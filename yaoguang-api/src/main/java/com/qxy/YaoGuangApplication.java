package com.qxy;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@NacosPropertySource(dataId = "wechat-server-config.yml", autoRefreshed = true, type = ConfigType.YAML, groupId = "WECHAT_SERVER")
public class YaoGuangApplication {

    public static void main(String[] args) {

        SpringApplication.run(YaoGuangApplication.class, args);

    }

}
