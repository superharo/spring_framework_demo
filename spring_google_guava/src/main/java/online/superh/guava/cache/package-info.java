/**
 * Guava Cache 是一套非常完善的本地缓存机制。
 * 功能实现
 * 缓存过期和淘汰机制
 * 并发处理能力
 * 更新锁定：类似于分布式锁，作用体现于对同一个 key，只让一个请求去读源并回填缓存，其他请求阻塞等待。
 * 集成数据源：get 可以集成数据源，在从缓存中读取不到时从数据源中读取数据并回填缓存
 * 监控缓存加载 / 命中情况
 * 本地缓存的应用场景
 * 对性能有非常高的要求
 * 不经常变化
 * 占用内存不大
 * 有访问整个集合的需求
 * 数据允许不时时一致
 */
package online.superh.guava.cache;

