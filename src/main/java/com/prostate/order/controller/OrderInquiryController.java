package com.prostate.order.controller;

import com.prostate.order.entity.OrderInquiry;
import com.prostate.order.service.OrderInquiryService;
import com.prostate.order.util.UUIDTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

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
        //创建订单完成,扣钱操作

        return insertFailedResponse();
    }
     /**
         *    @Description:  微信用户查询订单 (这里包括已完成,为完成的订单)
         *    @Date:  16:38  2018/7/17
         *    @Params:   * @param null
         */
     
    @PostMapping(value = "listByWechatUser")
    public Map listByWechatUser(@RequestParam String patientId){
        if (patientId==null||patientId.equals("")){
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry=new OrderInquiry();
        orderInquiry.setPatientId(patientId);
        List<OrderInquiry> list=orderInquiryService.selectByParams(orderInquiry);
        //如果未完成订单,患者端看不到医生编辑的内容
        for (OrderInquiry order:list){
            if (order.getOrderStatus().equals("0")){
                //患者端查询
                order.setProblemDescription("内心等待医生回复");
            }
        }
        if (list==null||list.equals("")){
            return queryEmptyResponse();
        }else if (list.size()==0){
            return queryEmptyResponse();
        }else {
            return querySuccessResponse(list);
        }
    }
     /**
         *    @Description:  医生端查询未完成订单
         *    @Date:  17:54  2018/7/17
         *    @Params:   * @param null
         */

     @PostMapping(value = "listByDoctorUnfinished")
     public Map listByDoctorUnfinished(@RequestParam String doctorId){
         if (doctorId==null||doctorId.equals("")){
             return emptyParamResponse();
         }
         OrderInquiry orderInquiry=new OrderInquiry();
         orderInquiry.setDoctorId(doctorId);
         orderInquiry.setOrderStatus("0");
         List<OrderInquiry> list=orderInquiryService.selectByParams(orderInquiry);
         if (list==null||list.equals("")){
             return queryEmptyResponse();
         }else if (list.size()==0){
             return queryEmptyResponse();
         }else {
             return querySuccessResponse(list);
         }
     }



    /**
     *    @Description:  医生端 查询已完成订单
     *    @Date:  17:54  2018/7/17
     *    @Params:   * @param null
     */

    @PostMapping(value = "listByDoctorFinished")
    public Map listByDoctorFinished(@RequestParam String doctorId){
        if (doctorId==null||doctorId.equals("")){
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry=new OrderInquiry();
        orderInquiry.setDoctorId(doctorId);
        orderInquiry.setOrderStatus("1");
        List<OrderInquiry> list=orderInquiryService.selectByParams(orderInquiry);
        if (list==null||list.equals("")){
            return queryEmptyResponse();
        }else if (list.size()==0){
            return queryEmptyResponse();
        }else {
            return querySuccessResponse(list);
        }
    }

    /**
     *    @Description:  ID查询订单详情
     *    @Date:  17:54  2018/7/17
     *    @Params:   * @param null
     */


    
         /**
             *    @Description:  订单修改
             *    @Date:  17:55  2018/7/17
             *    @Params:   * @param null
             */
         
}
