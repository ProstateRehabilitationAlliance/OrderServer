package com.prostate.order.feignService.impl;

import com.prostate.order.feignService.RecordServer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: developerxiaofeng
 * @Description: 调用 RecordServer 中的 API
 * @Date: Created in 14:52 2018/7/24
 */
@Component
public class RecordServerHystrix extends BaseServerHystrix implements RecordServer {
    @Override
    public Map addUserPatient(String patientId, String userId, String patientSource) {
        return resultMap;
    }

    @Override
    public Map getPatientDetailById(String patientId) {
        return resultMap;
    }
}
