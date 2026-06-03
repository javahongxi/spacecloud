package org.hongxi.cloud.sample.provider.dubbo.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.remoting.http12.HttpMethods;
import org.apache.dubbo.remoting.http12.rest.Mapping;
import org.apache.dubbo.remoting.http12.rest.Param;
import org.apache.dubbo.remoting.http12.rest.ParamType;
import org.hongxi.cloud.sample.api.EchoRequest;
import org.hongxi.cloud.sample.api.RestDemoService;

@Mapping("/api")
@DubboService
public class RestDemoServiceImpl implements RestDemoService {
    /**
     * GET /api/hello/{name} —— PathVariable 示例
     * curl http://localhost:50051/api/hello/lily
     */
    @Mapping(path = "/hello/{name}", method = HttpMethods.GET)
    @Override
    public String hello(@Param(value = "name", type = ParamType.PathVariable) String name) {
        return "Hello, " + name;
    }

    /**
     * GET /api/add?a=1&b=2 —— 多个 QueryParam 示例
     * curl 'http://localhost:50051/api/add?a=1&b=2'
     */
    @Mapping(path = "/add", method = HttpMethods.GET)
    @Override
    public int add(@Param(value = "a", type = ParamType.Param) int a,
                   @Param(value = "b", type = ParamType.Param) int b) {
        return a + b;
    }

    /**
     * POST /api/echo —— JSON Body 示例
     * curl -X POST http://localhost:50051/api/echo -H "Content-Type: application/json" -d '{"message":"hi"}'
     */
    @Mapping(path = "/echo", method = HttpMethods.POST)
    @Override
    public String echo(@Param(type = ParamType.Body) EchoRequest request) {
        return "Echo: " + request.getMessage();
    }

    /**
     * GET /api/greet/{name}?lang=zh —— PathVariable + QueryParam 示例
     * curl 'http://localhost:50051/api/greet/lily?lang=zh'
     */
    @Mapping(path = "/greet/{name}", method = HttpMethods.GET)
    @Override
    public String greet(@Param(value = "name", type = ParamType.PathVariable) String name,
                        @Param(value = "lang", type = ParamType.Param) String lang) {
        if ("zh".equals(lang)) {
            return "你好, " + name;
        }
        return "Hello, " + name;
    }
}
