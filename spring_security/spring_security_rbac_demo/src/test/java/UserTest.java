import online.superh.springsecurity.rbac.RbacApplication;
import online.superh.springsecurity.rbac.mapper.SysUserMapper;
import online.superh.springsecurity.rbac.pojo.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 17:25
 */
@SpringBootTest(classes = RbacApplication.class)
public class UserTest {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Test
    public void test_add_user(){
        SysUser sysUser = new SysUser();
        sysUser.setDeptId(0L);
        sysUser.setUserName("admin");
        sysUser.setNickName("admin");
        sysUser.setUserType("00");
        sysUser.setEmail("123456789@qq.com");
        sysUser.setPhonenumber("12345678901");
        sysUser.setSex("0");
        sysUser.setAvatar("头像");
        sysUser.setPassword("admin123");
        sysUser.setStatus("0");
        sysUser.setDelFlag("0");
        sysUser.setLoginIp("192.168.1.1");
        sysUser.setLoginDate(new Date());
        sysUser.setCreateBy("admin");
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateBy("admin");
        sysUser.setUpdateTime(new Date());
        sysUser.setRemark("admin");
        sysUserMapper.insert(sysUser);
    }

}
