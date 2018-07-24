package com.prostate.order.feignService;

import com.prostate.order.feignService.impl.RecordServerHystrix;
import com.prostate.order.feignService.impl.StaticServerHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: developerxiaofeng
 * @Description:   调用 RecordServer 中的 API
 * @Date: Created in 14:47 2018/7/24
 */
@FeignClient(value = "record-server", fallback = RecordServerHystrix.class)
public interface RecordServer {
    @PostMapping(value = "/userPatient/addUserPatient")
    Map addUserPatient(@RequestParam("patientId") String patientId,
                       @RequestParam("userId") String userId,
                       @RequestParam("patientSource") String patientSource);

}
