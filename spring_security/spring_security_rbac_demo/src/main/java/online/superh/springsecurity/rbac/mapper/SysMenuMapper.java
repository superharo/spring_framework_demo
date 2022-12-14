package online.superh.springsecurity.rbac.mapper;

import online.superh.springsecurity.rbac.pojo.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 22497
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2022-12-14 14:59:55
* @Entity online.superh.springsecurity.rbac.online.superh.springsecurity.rbac.SysMenu
*/
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<String> selectMenuPermsByUserId(Long userId);

}




