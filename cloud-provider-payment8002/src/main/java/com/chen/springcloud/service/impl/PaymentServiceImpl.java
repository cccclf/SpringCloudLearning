package com.chen.springcloud.service.impl;

import com.chen.springcloud.dao.PaymentDao;
import com.chen.springcloud.entities.Payment;
import com.chen.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {

    //service调dao
    @Resource
    private PaymentDao paymentDao;

    //写操作
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    //读操作
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentById(id);
    }

}
