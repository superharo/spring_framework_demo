import online.superh.springjedis.JedisApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-13 13:21
 */
@SpringBootTest(classes = JedisApplication.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void test01(){
        stringRedisTemplate.opsForValue().set("haro", "shuai");
    }

}
