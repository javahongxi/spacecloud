package org.hongxi.cloud.sample.provider.dubbo.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.hongxi.cloud.sample.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by javahongxi on 2026/6/1.
 */
@DubboService
public class DemoServiceImpl implements DemoService {

    private static final Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String sayHello(String name) {
        log.info("Provider received request, name: {}", name);
        return "Hello, " + name + ", Here is " + RpcContext.getServerContext().getLocalPort();
    }
}
