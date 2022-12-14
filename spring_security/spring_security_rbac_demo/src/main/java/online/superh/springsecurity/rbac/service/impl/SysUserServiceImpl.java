package online.superh.springsecurity.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.superh.springsecurity.rbac.mapper.SysUserMapper;
import online.superh.springsecurity.rbac.pojo.SysUser;
import online.superh.springsecurity.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 22497
* @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
* @createDate 2022-12-14 14:59:55
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SysUser selectUserByUserName(String username) {
        return userMapper.selectUserByUserName(username);
    }
}




