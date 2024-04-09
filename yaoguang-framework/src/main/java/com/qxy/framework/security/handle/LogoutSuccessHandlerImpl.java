package com.qxy.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.qxy.common.constant.Constants;
import com.qxy.common.core.domain.Result;
import com.qxy.common.core.domain.model.LoginUser;
import com.qxy.common.utils.common.MessageUtils;
import com.qxy.common.utils.common.StringUtils;
import com.qxy.common.utils.http.ServletUtils;
import com.qxy.framework.manager.AsyncManager;
import com.qxy.framework.manager.factory.AsyncFactory;
import com.qxy.framework.web.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 自定义退出处理类 返回成功
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{

    private TokenService tokenService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 退出处理
     *
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(Result.success(MessageUtils.message("user.logout.success"))));
    }
}
