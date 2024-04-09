package com.qxy.api.controller.system;

import com.qxy.common.constant.Constants;
import com.qxy.common.core.domain.Result;
import com.qxy.common.core.domain.entity.SysMenu;
import com.qxy.common.core.domain.entity.SysUser;
import com.qxy.common.core.domain.model.LoginBody;
import com.qxy.common.utils.security.SecurityUtils;
import com.qxy.framework.web.service.SysLoginService;
import com.qxy.framework.web.service.SysPermissionService;
import com.qxy.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysMenuService menuService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginBody loginBody)
    {
        Result ajaxResult = Result.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword());
        ajaxResult.put(Constants.TOKEN, token);
        return ajaxResult;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public Result getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("roles", roles);
        data.put("permissions", permissions);
        return Result.success(data);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public Result getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return Result.success(menuService.buildMenus(menus));
    }
}
