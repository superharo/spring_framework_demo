package online.superh.springsecurity.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.superh.springsecurity.rbac.mapper.SysRoleMapper;
import online.superh.springsecurity.rbac.pojo.SysRole;
import online.superh.springsecurity.rbac.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 22497
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
* @createDate 2022-12-14 14:59:55
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<String> selectRolePermissionByUserId(Long userId) {
        return sysRoleMapper.selectRolePermissionByUserId(userId);
    }
}




