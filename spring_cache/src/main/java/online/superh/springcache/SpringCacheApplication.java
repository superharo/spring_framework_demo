package online.superh.springcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//启用缓存
@EnableCaching
//mybatis扫描
@MapperScan(basePackages = "online.superh.springcache.mapper")
public class SpringCacheApplication {
    public static void main(String[] args) {
       SpringApplication.run(SpringCacheApplication.class,args);
    }
}