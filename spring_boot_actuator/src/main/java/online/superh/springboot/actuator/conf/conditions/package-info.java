/**
 * conditions 端点，对应 GET /actuator/conditions 接口，对应 ConditionsReportEndpoint 类。
 * 用于获得 Spring Boot 基于 Condition 条件创建 Bean 的情况。通过它，我们可以知道：
 * 如果一个 Bean 被创建，是因为满足了什么条件。
 * 如果一个 Bean 未被创建，是因为不满足什么条件。
 * 也就是说，通过 conditions 端点，我们可以排查基于 Condition 的 Bean 为什么被创建，为什么没被创建。
 * 在 Spring Boot 提供自动化配置的遍历的同时，也会带给我们为什么某个 Bean 被创建或未被创建的困扰，通过 conditions 端点就可以很好的排查和解决了。
 */
package online.superh.springboot.actuator.conf.conditions;