/**
 * heapdump 端点，对应 GET /actuator/heapdump 接口，对应 HeapDumpWebEndpoint 类。
 * 用于获取应用的 JVM Heap Dump（堆转储文件）。
 * heapdump 端点通过调用 HotSpotDiagnosticMXBean#dumpHeap(String outputFile, boolean live) 方法，进行 JVM Heap Dump 的获取。
 */
package online.superh.springboot.actuator.conf.heapdump;