package com.qxy.common.core.controller;

import com.github.pagehelper.PageInfo;
import com.qxy.common.constant.HttpStatus;
import com.qxy.common.core.domain.Result;
import com.qxy.common.core.domain.model.LoginUser;
import com.qxy.common.core.page.TableDataInfo;
import com.qxy.common.utils.common.StringUtils;
import com.qxy.common.utils.page.PageUtils;
import com.qxy.common.utils.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;


public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }


    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public Result success()
    {
        return Result.success();
    }

    /**
     * 返回失败消息
     */
    public Result error()
    {
        return Result.error();
    }

    /**
     * 返回成功消息
     */
    public Result success(String message)
    {
        return Result.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public Result success(Object data)
    {
        return Result.success(data);
    }

    /**
     * 返回失败消息
     */
    public Result error(String message)
    {
        return Result.error(message);
    }

    /**
     * 返回警告消息
     */
    public Result warn(String message)
    {
        return Result.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected Result toAjax(int rows)
    {
        return rows > 0 ? Result.success() : Result.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected Result toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId()
    {
        return getLoginUser().getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Long getDeptId()
    {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }
}
