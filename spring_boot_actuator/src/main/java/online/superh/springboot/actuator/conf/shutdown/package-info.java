/**
 * 用于远程优雅关闭应用。shutdown 端点通过调用 Spring ConfigurableApplicationContext#close() 方法，进行应用的优雅关闭。
 * 因为 shutdown 端点可以实现应用的关闭，是个相对敏感的操作，所以默认情况下是关闭的。
 * 所以，我们需要在配置文件中，配置 management.endpoint.shutdown.enabled = true 来进行开启。
 * 另外，在生产环境下，一定要做好 shutdown 端点的安全防护措施，例如：使用 Spring Security 进行安全认证、仅允许内网 IP 访问等等。
 */
package online.superh.springboot.actuator.conf.shutdown;