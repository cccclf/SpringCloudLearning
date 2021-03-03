package com.chen.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //访问localhost:9527/guonei 将会转发到 https://news.baidu.com/guonei 这个地址
        routes.route("path_route_chen",r -> r.path("/guonei").uri("https://news.baidu.com/guonei"));
        return routes.build();
    }

    @Bean
    public RouteLocator customRouteLocator1(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //访问localhost:9527/guonei 将会转发到 https://news.baidu.com/guonei 这个地址
        routes.route("path_route_chen1",r -> r.path("/guoji").uri("https://news.baidu.com/guoji"));
        return routes.build();
    }
}
