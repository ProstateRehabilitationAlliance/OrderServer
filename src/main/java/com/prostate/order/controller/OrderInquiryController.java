package com.prostate.order.controller;

import com.prostate.order.entity.OrderInquiry;
import com.prostate.order.service.OrderInquiryService;
import com.prostate.order.util.UUIDTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 13:20 2018/7/17
 */
@Slf4j
@RestController
@RequestMapping(value = "OrderInquiry")
public class OrderInquiryController extends BaseController {
    @Autowired
    private OrderInquiryService orderInquiryService;

         /**
             *    @Description:  创建订单
             *    @Date:  13:36  2018/7/17
             *    @Params:   * @param null
             */
         
    @PostMapping(value = "insert")
    public Map insert(@Validated OrderInquiry orderInquiry){
        orderInquiry.setId(UUIDTool.getUUID());
        System.out.println(orderInquiry.getId());
       int result= orderInquiryService.insertSelective(orderInquiry);
        if(result>=0){
            return insertSuccseeResponse("");
        }
        return insertFailedResponse();
    }
     /**
         *    @Description:  微信用户查询订单 (这里包括已完成,为完成的订单)
         *    @Date:  16:38  2018/7/17
         *    @Params:   * @param null
         */
     
    @PostMapping(value = "listByWechatUser")
    public Map listByWechatUser(@RequestParam String patientId){
        OrderInquiry orderInquiry=new OrderInquiry();
        orderInquiry.setPatientId(patientId);
        List<OrderInquiry> list=orderInquiryService.selectByParams(orderInquiry);
        if (list==null||list.equals("")){
            return queryEmptyResponse();
        }else if (list.size()==0){
            return queryEmptyResponse();
        }else {
            return querySuccessResponse(list);
        }
    }


}
