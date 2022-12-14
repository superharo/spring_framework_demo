package online.superh.springsecurity.rbac.mapper;

import online.superh.springsecurity.rbac.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 22497
* @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
* @createDate 2022-12-14 14:59:55
* @Entity online.superh.springsecurity.rbac.online.superh.springsecurity.rbac.SysUser
*/
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser selectUserByUserName(String username);

}




