package online.superh.springcache.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.superh.springcache.pojo.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
* @author 22497
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-12-13 10:25:46
* @Entity generator.online.superh.springcache.User
*/
@Repository
@CacheConfig(cacheNames = "users")
public interface UserMapper extends BaseMapper<User> {

    // 优先读缓存
    @Cacheable(key = "#id")
    User selectById(Integer id);

    // 主动写缓存
    @CachePut(key = "#user.id")
    default User insert0(User user) {
        // 插入记录
        this.insert(user);
        // 返回用户
        return user;
    }

    // 删除缓存
    @CacheEvict(key = "#id")
    int deleteById(Integer id);

}




