package online.superh.springsecurity.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.superh.springsecurity.rbac.pojo.SysRoleMenu;
import online.superh.springsecurity.rbac.service.SysRoleMenuService;
import online.superh.springsecurity.rbac.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 22497
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2022-12-14 14:59:55
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService{

}




