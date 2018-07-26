package com.prostate.order.feignService.impl;

import com.prostate.order.feignService.ThirdServer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: developerxiaofeng
 * @Description:
 * @Date: Created in 15:22 2018/7/25
 */
@Component
public class ThirdServerHystrix extends BaseServerHystrix implements ThirdServer {

    @Override
    public Map sendPaymentSuccess(String phoneNumber, String paramTime) {
        return resultMap;
    }

    @Override
    public Map sendInquiryEndToDoctor(String phoneNumber) {
        return resultMap;
    }
}
