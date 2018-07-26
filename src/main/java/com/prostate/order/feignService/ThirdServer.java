package com.prostate.order.feignService;

import com.prostate.order.feignService.impl.ThirdServerHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: developerxiaofeng
 * @Description:
 * @Date: Created in 15:20 2018/7/25
 */
@FeignClient(value = "third-server",fallback = ThirdServerHystrix.class)
public interface ThirdServer {

    @PostMapping(value = "/sms/sendPaymentSuccess")
    Map sendPaymentSuccess(@RequestParam("phoneNumber") String phoneNumber,
                           @RequestParam("paramTime") String paramTime);

    @PostMapping(value = "/sms/sendInquiryEndToDoctor")
    Map sendInquiryEndToDoctor(@RequestParam("phoneNumber") String phoneNumber);

}
