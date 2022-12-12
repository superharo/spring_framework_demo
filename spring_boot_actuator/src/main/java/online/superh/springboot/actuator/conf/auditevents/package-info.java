/**
 * auditevents 端点，对应 GET /actuator/auditevents 接口，对应 AuditEventsEndpoint 类。
 * 用于获取应用的最近的 审计事件( AuditEvents )。
 * httptrace 端点通过 AuditEventRepository 获取最近的 AuditEvents。
 * Actuator 内置了 InMemoryAuditEventRepository 实现类，存储最近的 1000 条 AuditEvents 到内存中。
 */
package online.superh.springboot.actuator.conf.auditevents;