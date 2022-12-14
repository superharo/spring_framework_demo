package online.superh.springsecurity.rbac.controller;


import online.superh.springsecurity.rbac.common.Constants;
import online.superh.springsecurity.rbac.common.ServletUtils;
import online.superh.springsecurity.rbac.config.security.LoginUser;
import online.superh.springsecurity.rbac.pojo.SysUser;
import online.superh.springsecurity.rbac.service.SysMenuService;
import online.superh.springsecurity.rbac.config.security.SysLoginService;
import online.superh.springsecurity.rbac.config.security.SysPermissionService;
import online.superh.springsecurity.rbac.config.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    @PostMapping("/login")
    public Map<String, Object> login(String username, String password, String code, String uuid) {
        Map<String, Object> result = new HashMap<>();
        // 生成令牌
        String token = loginService.login(username, password, code, uuid);
        result.put(Constants.TOKEN, token);
        return result;
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

        /**
         * 获取用户信息
         *
         * @return 用户信息
         */
    // 在 Spring EL 表达式中，调用指定 Bean 名字的方法时，使用 @ + Bean 的名字。
    // 在 RuoYi-Vue 中，声明 PermissionService 的 Bean 名字为 ss 。
    @PreAuthorize("@ss.hasPermi('system:getInfo')")
    @GetMapping("getInfo")
    public Map<String, Object>  getInfo() {
        // 获得当前 LoginUser
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色标识的集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        // 返回结果
        Map<String, Object>  ajax = new HashMap<String, Object>();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    // @GetMapping("getRouters")
    // public Map<String, Object>  getRouters() {
    //     // 获得当前 LoginUser
    //     LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
    //     // 获得用户的 SysMenu 数组
    //     SysUser user = loginUser.getUser();
    //     List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
    //     // 构建路由 RouterVo 数组。可用于 Vue 构建管理后台的左边菜单
    //     return AjaxResult.success(menuService.buildMenus(menus));
    // }

}
