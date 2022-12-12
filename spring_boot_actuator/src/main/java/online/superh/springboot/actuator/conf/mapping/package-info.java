/**
 * mappings 端点，对应 GET /actuator/mappings 接口，对应 MappingsEndpoint 类。
 * 用于获取应用的 HTTP 请求匹配( Mapping )信息。
 * mappings 端点通过 MappingDescriptionProvider ，获取不同类型的 HTTP 请求匹配信息。
 */
package online.superh.springboot.actuator.conf.mapping;