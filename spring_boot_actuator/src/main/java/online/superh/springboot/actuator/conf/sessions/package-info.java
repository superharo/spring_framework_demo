/**
 * sessions 端点，对应 SessionsEndpoint 类。对应接口如下：
 * GET /actuator/sessions/?username= 接口，获取指定 username 对应的 Session 。
 * GET /actuator/sessions/{sessionId} 接口，获取指定 sessionId 对应的 Session 。
 * DELETE /actuator/sessions/{sessionId} 接口，删除指定 sessionId 对应的 Session 。
 */
package online.superh.springboot.actuator.conf.sessions;