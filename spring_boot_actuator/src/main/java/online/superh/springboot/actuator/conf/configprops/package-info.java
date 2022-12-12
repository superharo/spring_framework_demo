/**
 * configprops 端点，对应 GET /actuator/configprops 接口，对应 ConfigurationPropertiesReportEndpoint 类。
 * 用于获取应用中所有有效的 Spring @ConfigurationProperties 注解的配置属性类。
 * 例如说，"spring.jdbc" 配置对应的 JdbcProperties 配置属性类。
 */
package online.superh.springboot.actuator.conf.configprops;