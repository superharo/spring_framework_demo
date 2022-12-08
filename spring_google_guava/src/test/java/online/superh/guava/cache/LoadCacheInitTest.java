package online.superh.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class LoadCacheInitTest {

    /**
     * LoadingCache是Cache的子接口，
     * 相比较于 Cache，
     * 当从LoadingCache中读取一个指定key的记录时，
     * 如果该记录不存在，则LoadingCache可以自动执行加载数据到缓存的操作。
     */
    @Test
    @SneakyThrows
    public void testBuild() {

        //LoadingCache类型的对象也是通过CacheBuilder进行构建，
        // 不同的是，在调用CacheBuilder的build方法时，必
        // 须传递一个CacheLoader类型的参数，
        // CacheLoader 的load方法需要我们提供实现
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            public String load(String key) throws Exception {
                Thread.sleep(1000); //休眠1s，模拟加载数据
                System.out.println(key + " is loaded from a cacheLoader!");
                return key + "'s value";
            }
        };

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .build(loader);//在构建时指定自动加载器

        loadingCache.get("key1");
        loadingCache.get("key2");
        loadingCache.get("key3");
    }

}