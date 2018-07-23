package com.prostate.order.controller;

import com.alibaba.fastjson.JSON;
import com.prostate.order.entity.*;
import com.prostate.order.feignService.WalletServer;
import com.prostate.order.service.OrderInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private WalletServer walletServer;


        /**
            *    @description:   创建订单
            *    @date:  16:52  2018/7/23
            *    @param:   * @param orderInquiry
            */

    @PostMapping(value = "insert")
    public Map insert(@Validated({GroupOutId.class}) OrderInquiry orderInquiry){

       int result= orderInquiryService.insertSelective(orderInquiry);
        if(result>=0){
            return insertSuccseeResponse(null);
        }
        //创建订单完成,扣钱操作

        return insertFailedResponse();
    }


        /**
            *    @description:  微信用户查询订单 (这里包括已完成,为完成的订单)
            *    @date:  16:53  2018/7/23
            *    @param:   * @param null
            */

    @PostMapping(value = "listByWechatUser")
    public Map listByWechatUser(String token){
        if (token==null||token.equals("")){
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry=new OrderInquiry();
        orderInquiry.setPatientId(token);
        List<OrderInquiry> orderInquiries=orderInquiryService.selectByParams(orderInquiry);
        //如果未完成订单,患者端看不到医生编辑的内容
        for (OrderInquiry order:orderInquiries){
            if (order.getOrderStatus().equals("00")||
                    order.getOrderStatus().equals("01")){
                //患者端查询
                order.setDoctorResponse("内心等待医生回复");
            }
        }
        if (orderInquiries.isEmpty()==true){
            return queryEmptyResponse();
        }else {
            return querySuccessResponse(orderInquiries);
        }
    }


        /**
            *    @description:      医生端查询未完成订单
            *    @date:  16:53  2018/7/23
            *    @param:   * @param null
            */

     @PostMapping(value = "listByDoctorUnfinished")
     public Map listByDoctorUnfinished(String token){
         if (token==null||token.equals("")){
             return emptyParamResponse();
         }
         //查询来自网络订单
         OrderInquiry orderInquiry=new OrderInquiry();
         orderInquiry.setDoctorId(token);
         orderInquiry.setOrderStatus("00");
         List<OrderInquiry> ordersFromInternet=orderInquiryService.selectByParams(orderInquiry);
         //查询转诊订单
         OrderInquiry inquiry=new OrderInquiry();
         orderInquiry.setDoctorId(token);
         orderInquiry.setOrderStatus("01");
         List<OrderInquiry> ordersFromTransferingConsultation=orderInquiryService.selectByParams(orderInquiry);
         ordersFromInternet.addAll(ordersFromTransferingConsultation);
         if (ordersFromInternet.isEmpty()==true){
             return queryEmptyResponse();
         }else {
             return querySuccessResponse(ordersFromInternet);
         }
     }





       /**
           *    @description:       医生端 查询已完成订单
           *    @date:  16:53  2018/7/23
           *    @param:   * @param token
           */

    @PostMapping(value = "listByDoctorFinished")
    public Map listByDoctorFinished(String token){
        if (token==null||token.equals("")){
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry=new OrderInquiry();
        orderInquiry.setDoctorId(token);
        orderInquiry.setOrderStatus("1");
        List<OrderInquiry> list=orderInquiryService.selectByParams(orderInquiry);
        if (list.isEmpty()==true){
            return queryEmptyResponse();
        }else {
            return querySuccessResponse(list);
        }
    }


       /**
           *    @description:       ID查询订单详情
           *    @date:  16:54  2018/7/23
           *    @param:   * @param id
           */

    @GetMapping(value = "getOrderById")
    public Map getOrderById(String id){
           if (id==null||id.equals("")){
               return emptyParamResponse();
           }
           OrderInquiry orderInquiry=orderInquiryService.selectById(id);
           if (orderInquiry==null||orderInquiry.equals("")){
               return queryEmptyResponse();
           }else {
               return querySuccessResponse(orderInquiry);
           }

        }

    

         /**
             *    @description:     医生端订单修改
             *    @date:  16:54  2018/7/23
             *    @param:   * @param null
             */

     @PostMapping(value = "updateOrder")
    public  Map updateOrder(@Validated({GroupId.class,GroupOutId.class}) OrderInquiry orderInquiry){
         //需要验证一下传过来的id  医生id 患者id 是否正确
         OrderInquiry inquiry = orderInquiryService.selectById(orderInquiry.getId());
         if (inquiry==null||inquiry.equals("")){
             return queryEmptyResponse();
         }else {
            if (inquiry.getDoctorId().equals(orderInquiry.getDoctorId())){

            }else {
                return queryEmptyResponse();
            }
         }
        int result=orderInquiryService.updateSelective(orderInquiry);
        if (result>0){
            //这里做个判断,如果orderInquiry的状态  为已完成,需要调用钱包服务
            if (orderInquiry.getOrderStatus().equals("1")){
                DoctorWallet doctorWallet=new DoctorWallet();
                //查询钱包id
                Map map = walletServer.selectByDoctorId(orderInquiry.getDoctorId());
                if (map.get("code").equals("40004")){
                    return requestCustomResponse("该医生没有创建钱包",null);
                }else if (!map.get("code").equals("20000")){
                    return map;
                }
                String jsonString = JSON.toJSONString(map.get("result"));
                DoctorWallet wallet= JSON.parseObject(jsonString, DoctorWallet.class);
                doctorWallet.setId(wallet.getId());
                doctorWallet.setDoctorId(orderInquiry.getDoctorId());
                doctorWallet.setWalletBalance(orderInquiry.getOrderPrice());
                Map m=walletServer.updateBalance(doctorWallet);
                log.info(m.toString());
                //添加交易记录
                ReceiptPayment receiptPayment=new ReceiptPayment();
                receiptPayment.setWalletId(wallet.getId());
                //医生钱包增加
                receiptPayment.setReceiptPaymentType("支付");
                receiptPayment.setTransactionAmount(orderInquiry.getOrderPrice());
                receiptPayment.setPaymentType("银行卡");
                //查询患者名字  未做
                receiptPayment.setRemark("患者端"+orderInquiry.getPatientId()+" 支付"+orderInquiry.getOrderPrice());

                Map  n=walletServer.save(receiptPayment);
                log.info(n.toString());
            }

            return updateSuccseeResponse();
        }else {
            return updateFailedResponse();
        }



     }

         
}
