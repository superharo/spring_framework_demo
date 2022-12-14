package online.superh.springsecurity.rbac.config.security;


import online.superh.springsecurity.rbac.pojo.SysUser;
import online.superh.springsecurity.rbac.service.SysMenuService;
import online.superh.springsecurity.rbac.service.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Component
public class SysPermissionService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        // if (user.isAdmin()) { // 如果是管理员，强制添加 admin 角色
        //     roles.add("admin");
        // } else { // 如果非管理员，进行查询
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        // }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (StringUtils.equals("admin",user.getUserName())) {
            roles.add("*:*:*"); // 所有模块
        } else {
            // 读取
            roles.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return roles;
    }

}
