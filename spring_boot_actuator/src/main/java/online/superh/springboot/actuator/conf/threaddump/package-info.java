/**
 * 用于获取应用的所有存活的线程信息。
 * threaddump 端点通过调用 ThreadMXBean#dumpAllThreads(boolean lockedMonitors, boolean lockedSynchronizers) 方法，进行线程信息的获取。
 */
package online.superh.springboot.actuator.conf.threaddump;