package org.hongxi.cloud.sample.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

import org.hongxi.cloud.sample.sentinel.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by javahongxi on 2026/6/1.
 */
@RestController
public class TestController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;

	@Autowired
	private ProviderClient providerClient;

	/**
	 * 基础资源示例：通过 @SentinelResource 手动定义资源名 "hello"，
	 * 可在 Sentinel Dashboard 中对该资源配置流控规则。
	 */
	@GetMapping("/hello")
	@SentinelResource("hello")
	public String hello() {
		return "Hello";
	}

	/**
	 * 热点参数流控示例：资源名 "aa"，可配合 param-flow 规则对参数 b/a 进行限流。
	 */
	@GetMapping("/aa")
	@SentinelResource("aa")
	public String aa(int b, int a) {
		return "Hello test";
	}

	/**
	 * 无注解保护：未加 @SentinelResource，不会被 Sentinel 自动埋点（Web URL 资源除外）。
	 */
	@GetMapping("/test")
	public String test1() {
		return "Hello test";
	}

	/**
	 * RestTemplate + Sentinel：通过 @SentinelRestTemplate 注解为 RestTemplate
	 * 添加流控拦截，被限流时执行 ExceptionUtil.handleException 降级逻辑。
	 */
	@GetMapping("/template")
	public String client() {
		return restTemplate.getForObject("http://www.taobao.com/test", String.class);
	}

	/**
	 * 慢调用熔断示例：通过 CircuitBreakerFactory 包装调用，
	 * 模拟 500ms 慢响应，触发 Sentinel 降级规则后返回 fallback 兜底值。
	 */
	@GetMapping("/slow")
	public String slow() {
		return circuitBreakerFactory.create("slow").run(() -> {
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "slow";
		}, throwable -> "fallback");
	}

	/**
	 * OpenFeign + Sentinel：通过 Feign 客户端调用 provider-sample，
	 * Sentinel 自动将 Feign 方法包装为资源（资源名格式：GET:/hello），
	 * 触发流控规则或服务不可用时自动调用 ProviderClientFallback 降级。
	 */
	@GetMapping("/feign/hello")
	public String feignHello(String name) {
		return providerClient.hello(name);
	}

	/**
	 * OpenFeign + Sentinel：调用 provider-sample 的 /echo 接口，
	 * 资源名为 GET:/echo，降级逻辑同上。
	 */
	@GetMapping("/feign/echo")
	public String feignEcho(String message) {
		return providerClient.echo(message);
	}

}
