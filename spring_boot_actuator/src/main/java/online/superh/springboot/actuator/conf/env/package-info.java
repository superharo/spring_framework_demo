/**
 *env 端点，对应 GET /actuator/env 和 GET /actuator/env/{name} 接口，对应 EnvironmentEndpoint 类。
 * 用于获取应用中所有可用的 Spring PropertySource 配置来源。
 * Spring Boot 对应的 PropertySource 非常多：
 * 应用的 YAML 配置文件、Properties 配置文件。例如说，application.yaml、application.properties 。
 * 命令行指定的参数。例如 java -jar springboot.jar --server.port=9090 。
 * Java 系统变量，即 System#getProperties() 。
 * 操作系统环境变量。
 */
package online.superh.springboot.actuator.conf.env;