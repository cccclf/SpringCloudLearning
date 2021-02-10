package com.chen.springcloud.service;

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
    public String paymentInfoTimeout(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:    " + Thread.currentThread().getName() + "    paymentInfoTimeout, id = " + id + "\t" + "after 3 seconds!";
    }


}
