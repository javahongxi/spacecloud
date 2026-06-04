package org.hongxi.cloud.sample.api;

public interface RestDemoService {

    String hello(String name);

    int add(int a, int b);

    String echo(EchoRequest request);

    String greet(String name, String lang);

    String trace(String traceparent);
}
