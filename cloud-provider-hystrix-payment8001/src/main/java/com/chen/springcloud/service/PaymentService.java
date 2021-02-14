package com.chen.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

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


}
