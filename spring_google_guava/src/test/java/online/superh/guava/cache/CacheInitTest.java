package online.superh.guava.cache;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class CacheInitTest {

    /**
     * 设置数量
     */
    @Test
    void limit() {
        // CacheBuilder类构建一个缓存对象
        Cache<String,String> cache = CacheBuilder.newBuilder()
                //  指定用于缓存的hash table最低总规模; 可以不设置
                .initialCapacity(0)
                // 指定缓存所能够存储的最大记录数量
                .maximumSize(2)
                .build();
        cache.put("key1","value1");
        cache.put("key2","value2");
        cache.put("key3","value3");
        // 当Cache中的记录数量达到最大值后再调用put方法向其中添加对象，
        // Guava 会先从当前缓存的对象记录中选择一条删除掉，腾出空间后再将新的对象存储到 Cache 中。
        System.out.println("第一个值：" + cache.getIfPresent("key1"));
        System.out.println("第二个值：" + cache.getIfPresent("key2"));
        System.out.println("第三个值：" + cache.getIfPresent("key3"));
    }

    /**
     * 设置过期时间
     * expireAfterWrite 指定对象被写入到缓存后多久过期
     * expireAfterAccess 指定对象多久没有被访问后过期
     */
    @Test
    @SneakyThrows
    void express(){
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();
        cache.put("key1", "value1");
        int time = 1;
        while (true) {
            System.out.println("第" + time++ + "次取到key1的值为：" + cache.getIfPresent("key1"));
            Thread.sleep(1000);
        }

    }

    /**
     * weakKeys
     * 将缓存中的key设置成weakKey模式。默认情况下，会使用 “强关系” 来保存 key 值。当设置为 weakKey 时，会使用（==）来匹配 key 值。
     * 在使用 weakKey 的情况下，数据可能会被 GC。
     * 数据被 GC 后，可能仍然会被 size 方法计数，但是对其执行 read 或 write 方法已经无效。
     * weakValues
     * 将缓存中的数据设置为weakValues模式。启用 weakValue 设置时，某些数据会被 GC。默认情况下，会使用 “强关系” 来保存 key 值。
     * 当设置为 weakValue 时，会使用（==）来匹配 value 值。
     * 数据被 GC 后，可能仍然会被 size 方法计数，但是对其执行 read 或 write 方法已经无效。
     * softValues
     * 将缓存中的数据设置为softValues模式。使用这个模式时，所有的数据都使用 SoftReference 类对缓存中的数据进行包裹（就是在 SoftReference 实例中存储真实的数据）。
     * 使用 SoftReference 包裹的数据，会被全局垃圾回收管理器托管，按照 LRU 的原则来定期 GC 数据。
     * 数据被 GC 后，可能仍然会被 size 方法计数，但是对其执行 read 或 write 方法已经无效。
     */
    @Test
    public void testWeak(){
        Cache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .weakValues()
                .build();
        Object value = new Object();
        cache.put("key1", value);
        value = new Object(); //原对象不再有强引用
        // 构建Cache时通过weakValues方法指定 Cache 只保存记录值的一个弱引用。
        // 当给 value 引用赋值一个新的对象之后，就不再有任何一个强引用指向原对象。
        // System.gc()触发垃圾回收后，原对象就被清除了。
        System.gc();
        System.out.println(cache.getIfPresent("key1"));
    }

    /**
     * invalidate 方法一次只能删除 Cache 中一个记录，接收的参数是要删除记录的 key。
     * invalidateAll 方法可以批量删除 Cache 中的记录，
     * 当没有传任何参数时，invalidateAll 方法将清除 Cache 中的全部记录。
     * invalidateAll 也可以接收一个Iterable类型的参数，参数中包含要删除记录的所有 key 值。
     */
    @Test
    public void testInvalid(){
        Cache<String, String> cache = CacheBuilder.newBuilder().build();
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        // cache.invalidate("key1"); // 清楚key1这个缓存
        List<String> list = Lists.newArrayList("key1", "key2");
        cache.invalidateAll(list); //批量清除list中全部key对应的记录
        System.out.println(cache.getIfPresent("key1"));
        System.out.println(cache.getIfPresent("key2"));
        System.out.println(cache.getIfPresent("key3"));
    }

    /**
     * 可以为Cache对象添加一个移除监听器，这样当有记录 (缓存) 被删除时可以感知到这个事件
     */
    @Test
    public void testRemoveListener(){
        // 初始化监听器
        RemovalListener<String, String> listener = notification ->
                System.out.println("[" + notification.getKey() + ":" + notification.getValue() + "] is removed!");

        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                // 挂载监听器
                .removalListener(listener)
                .build();
        // 因为maximumSize为3, 所以cache只能存三个缓存,后面的会覆盖前面的缓存
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value3");
        cache.put("key5", "value3");
        cache.put("key6", "value3");
        cache.put("key7", "value3");
        cache.put("key8", "value3");
    }

    /**
     * Guava 可以保证当有多个线程同时访问 Cache 中的一个 key 时，如果 key 对应的记录不存在，
     * Guava 只会启动一个线程执行 get 方法中 Callable 参数对应的任务加载数据存到缓存。
     * 当加载完数据后，任何线程中的 get 方法都会获取到 key 对应的值。
     */
    @Test
    public void testLoad() {

        final Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .build();

        new Thread(() -> {
            System.out.println("thread1");
            try {
                String value = cache.get("key", () -> {
                    System.out.println("load1"); //加载数据线程执行标志
                    Thread.sleep(1000); //模拟加载时间
                    return "auto load1 by Callable";
                });
                System.out.println("thread1 " + value);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("thread2");
            try {
                String value = cache.get("key", () -> {
                    System.out.println("load2"); //加载数据线程执行标志
                    Thread.sleep(1000); //模拟加载时间
                    return "auto load2 by Callable";
                });
                System.out.println("thread2 " + value);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Guava Cache 可以通过参数concurrencyLevel(5)设置并发度，即可以同时写缓存的线程数
     */
    @Test
    void testConcurrent(){
        Cache<String,String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .concurrencyLevel(5)
                .build();
        cache.put("key1","value1");
        cache.getIfPresent("key1");
    }

    /**
     * 数据清除权重 (maximumWeight, weigher)
     */
    @Test
    public void testWeight() {
        // LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
        //         .maximumWeight(100000)
        //         .weigher(new Weigher<Key, Graph>() {
        //             public int weigh(Key k, Graph g) {
        //                 return g.vertices().size();
        //             }
        //         })
        //         .build(
        //                 new CacheLoader<Key, Graph>() {
        //                     public Graph load(Key key) { // no checked exception
        //                         return createExpensiveGraph(key);
        //                     }
        //                 });
    }

}

