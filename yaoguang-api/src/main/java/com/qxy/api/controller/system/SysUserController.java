package com.qxy.api.controller.system;

import com.qxy.common.annotation.Log;
import com.qxy.common.core.controller.BaseController;
import com.qxy.common.core.domain.Result;
import com.qxy.common.core.domain.entity.SysDept;
import com.qxy.common.core.domain.entity.SysRole;
import com.qxy.common.core.domain.entity.SysUser;
import com.qxy.common.core.page.TableDataInfo;
import com.qxy.common.enums.BusinessType;
import com.qxy.common.utils.common.StringUtils;
import com.qxy.common.utils.poi.ExcelUtil;
import com.qxy.common.utils.security.SecurityUtils;
import com.qxy.framework.web.service.SysPermissionService;
import com.qxy.system.service.ISysDeptService;
import com.qxy.system.service.ISysPostService;
import com.qxy.system.service.ISysRoleService;
import com.qxy.system.service.ISysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysDeptService deptService;

    @PreAuthorize("@per.hasPermission('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@per.hasPermission('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@per.hasPermission('system:user:import')")
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        // 读excel表到对象中
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@per.hasPermission('system:user:query')")
    @GetMapping(value = {"/getUserInfo", "/getUserInfo/{userId}"})
    public Result getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        Result result = Result.success();
        List<SysRole> roles = roleService.selectRoleAll();
        result.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        result.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            result.put(Result.DATA_TAG, sysUser);
            result.put("postIds", postService.selectPostListByUserId(userId));
            result.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return result;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@per.hasPermission('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public Result add(@Validated @RequestBody SysUser user) {
        System.out.println(user);
        if (userService.checkUserNameUnique(user)) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@per.hasPermission('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (userService.checkUserNameUnique(user)) {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@per.hasPermission('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{userIds}")
    public Result remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return error("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@per.hasPermission('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    public Result resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@per.hasPermission('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    public Result changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @PreAuthorize("@per.hasPermission('system:user:query')")
    @GetMapping("/authRole/{userId}")
    public Result authRole(@PathVariable("userId") Long userId) {
        Result ajax = Result.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @PreAuthorize("@per.hasPermission('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/authRole")
    public Result insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@per.hasPermission('system:user:list')")
    @GetMapping("/deptTree")
    public Result deptTree(SysDept dept) {
        return success(deptService.selectDeptTreeList(dept));
    }
}
