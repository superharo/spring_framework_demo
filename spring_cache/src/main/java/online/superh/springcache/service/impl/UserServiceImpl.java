package online.superh.springcache.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.superh.springcache.mapper.UserMapper;
import online.superh.springcache.pojo.User;
import online.superh.springcache.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author 22497
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-12-13 10:25:46
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}




