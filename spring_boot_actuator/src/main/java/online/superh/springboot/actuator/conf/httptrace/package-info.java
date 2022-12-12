/**
 * 用于获取应用的最近的 HTTP 跟踪信息( HttpTrace )。
 * httptrace 端点通过 TraceRepository 获取最近的 HttpTrace。
 * Actuator 内置了 InMemoryHttpTraceRepository 实现类，存储最近的 100 条 HttpTrace 到内存中。
 */
package online.superh.springboot.actuator.conf.httptrace;