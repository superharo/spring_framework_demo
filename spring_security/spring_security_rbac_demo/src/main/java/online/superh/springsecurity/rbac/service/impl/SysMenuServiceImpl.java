package online.superh.springsecurity.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.superh.springsecurity.rbac.mapper.SysMenuMapper;
import online.superh.springsecurity.rbac.pojo.SysMenu;
import online.superh.springsecurity.rbac.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* @author 22497
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2022-12-14 14:59:55
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Override
    public Set<String>  selectMenuPermsByUserId(Long userId) {
        // 读取 SysMenu 的权限标识数组
        List<String> perms = sysMenuMapper.selectMenuPermsByUserId(userId);
        // 逐个，按照“逗号”分隔
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}




