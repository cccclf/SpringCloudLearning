package com.chen.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    //===========================服务降级===========================
    /**
     * 不会出错的方法
     * @param id
     * @return
     */
    public String paymentInfoOk(Integer id) {
        return "线程池:    " + Thread.currentThread().getName() + "    paymentInfoOk, id = " + id + "\t" + "success!";
    }

    /**
     * 添加延时，3秒后成功
     * @param id
     * @return
     */
    //fallbackMethod：备选的方法
    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler",commandProperties = {
            //设置自身调用超时时间的峰值，峰值内可以正常运行，超过了需要有备用的方法处理，作服务降级fallback
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String paymentInfoTimeout(Integer id) {
        int timeNumber = 3;
        //对错误逻辑的代码导致程序运行错误也会启动备选方案
//        int age = 10/0;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:    " + Thread.currentThread().getName() + "    paymentInfoTimeout, id = " + id + "\t" + "after "+ timeNumber + "seconds!";
    }

    public String paymentInfoTimeoutHandler(Integer id){
        return "线程池:    " + Thread.currentThread().getName() + " 系统繁忙，请稍后再试~~~(备选方法执行ing)";
    }

    //===========================服务熔断===========================

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            //该属性主要用于控制断路器功能是否生效，是否能够在断路器打开状态下路请求的执行
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//是否开启断路器
            //该属性主要表示在一个监控窗口内能够触发断路器打开的最小请求量阈值。
            // 听着很绕口是吧 ，意思就是如果监控最小窗口为1s，那么在这一秒内只有请求量超过这个阈值时，才进行错误率的判断，如果没有超过这个阈值，那么将永远都不会以触发熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),//请求次数
            //该属性表示断路器打开后，直接执行拒绝请求时间段，在此时间区间内，任何请求都将被直接Fast-Fail。
            // 只有当过了该时间段后，circuitBreaker就进入了“半开”状态，才允许一个请求通过尝试执行下游调用。
            // 如果该请求在执行过程中失败了，则circuitBreaker进入“打开”状态，如果成功了，则直接进入“关闭”
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间范围，断路器开启的时间，窗口期后，尝试请求是否成功，若成功，则会关闭熔断
            //该属性表示熔断触发条件，即触发熔断时错误百分比。一旦打到这个触发比例，断路器就进入“打开”状态，
            // 但前提是，需要整体的请求流量能够满足设置的容量——requestVolumeThreshold
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),//失败率达到多少后跳闸,60% / 错误百分比，默认是50%
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {

        if (id < 0){
            throw new RuntimeException("**********id 不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
    }
    //兜底的方法
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能为负数，请稍后再试~~~~， id = " + id;
    }

}
