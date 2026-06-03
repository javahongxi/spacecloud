package org.hongxi.cloud.sample.provider.dubbo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.hongxi.cloud.sample.api.DemoService;

/**
 * Created by javahongxi on 2026/6/1.
 */
@Slf4j
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        log.info("Provider received request, name: {}", name);
        return "Hello, " + name + ", Here is " + RpcContext.getServerContext().getLocalPort();
    }
}
