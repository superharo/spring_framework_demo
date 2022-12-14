package online.superh.springsecurity.rbac.mapper;

import online.superh.springsecurity.rbac.pojo.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 22497
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2022-12-14 14:59:55
* @Entity online.superh.springsecurity.rbac.online.superh.springsecurity.rbac.SysRole
*/
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<String> selectRolePermissionByUserId(Long userId);

}




