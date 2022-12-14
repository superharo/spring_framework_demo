package online.superh.springsecurity.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import online.superh.springsecurity.rbac.pojo.SysMenu;

import java.util.Set;

/**
* @author 22497
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2022-12-14 14:59:55
*/
public interface SysMenuService extends IService<SysMenu> {

    Set<String> selectMenuPermsByUserId(Long userId);
}
