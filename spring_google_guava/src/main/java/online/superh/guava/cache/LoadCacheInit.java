package online.superh.guava.cache;

import com.google.common.cache.LoadingCache;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-08 11:30
 */
public interface LoadCacheInit<K,V> {

    LoadingCache<K, V> builder();

}
