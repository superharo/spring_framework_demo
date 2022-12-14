package online.superh.springsecurity.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.superh.springsecurity.rbac.pojo.SysUserRole;
import online.superh.springsecurity.rbac.service.SysUserRoleService;
import online.superh.springsecurity.rbac.mapper.SysUserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 22497
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2022-12-14 14:59:55
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements SysUserRoleService{

}




