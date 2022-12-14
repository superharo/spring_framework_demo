package online.superh.springsecurity.rbac.service;

import online.superh.springsecurity.rbac.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 22497
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2022-12-14 14:59:55
*/
public interface SysUserService extends IService<SysUser> {

    SysUser selectUserByUserName(String username);
}
