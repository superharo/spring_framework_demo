package online.superh.springsecurity.rbac.mapper;

import online.superh.springsecurity.rbac.pojo.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 22497
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Mapper
* @createDate 2022-12-14 14:59:55
* @Entity online.superh.springsecurity.rbac.online.superh.springsecurity.rbac.SysUserRole
*/
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}




