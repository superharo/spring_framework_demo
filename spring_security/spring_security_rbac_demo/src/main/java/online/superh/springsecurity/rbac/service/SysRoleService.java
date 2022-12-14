package online.superh.springsecurity.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import online.superh.springsecurity.rbac.pojo.SysRole;

import java.util.List;

/**
* @author 22497
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2022-12-14 14:59:55
*/
public interface SysRoleService extends IService<SysRole> {

    List<String> selectRolePermissionByUserId(Long userId);
}
