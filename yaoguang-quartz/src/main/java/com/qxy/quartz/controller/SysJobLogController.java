package com.qxy.quartz.controller;

import com.qxy.common.annotation.Log;
import com.qxy.common.core.controller.BaseController;
import com.qxy.common.core.domain.Result;
import com.qxy.common.core.page.TableDataInfo;
import com.qxy.common.enums.BusinessType;
import com.qxy.quartz.domain.SysJobLog;
import com.qxy.quartz.service.ISysJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 */
@RestController
@RequestMapping("/jobLog")
public class SysJobLogController extends BaseController {
    @Autowired
    private ISysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @PreAuthorize("@per.hasPermission('monitor:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJobLog sysJobLog) {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }


    /**
     * 根据调度编号获取详细信息
     */
    @PreAuthorize("@per.hasPermission('monitor:job:query')")
    @GetMapping(value = "/getLogById/{jobLogId}")
    public Result getInfo(@PathVariable Long jobLogId) {
        return success(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 删除定时任务调度日志
     */
    @PreAuthorize("@per.hasPermission('monitor:job:remove')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public Result remove(@PathVariable Long[] jobLogIds) {
        return toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     */
    @PreAuthorize("@per.hasPermission('monitor:job:remove')")
    @Log(title = "调度日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Result clean() {
        jobLogService.cleanJobLog();
        return success();
    }
}
