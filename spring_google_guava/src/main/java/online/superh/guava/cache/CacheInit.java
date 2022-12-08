package online.superh.guava.cache;

import com.google.common.cache.Cache;

/**
 * @version: 1.0
 * @author: haro
 * @description: Cache 初始化学习
 * @date: 2022-12-08 10:54
 */
public interface CacheInit<K,V> {

   /**
    * 构建 cach
    * @return
    */
   Cache<K, V>  builder();



}
