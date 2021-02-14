package com.chen.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfoOk(Integer id) {
        return "from PaymentFallbackService with the method==paymentInfoOk";
    }

    @Override
    public String paymentInfoTimeout(Integer id) {
        return "from PaymentFallbackService with the method==paymentInfoTimeout";
    }
}
