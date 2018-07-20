package com.prostate.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prostate.order.entity.*;
import com.prostate.order.feignService.StaticServer;
import com.prostate.order.feignService.WalletServer;
import com.prostate.order.service.OrderInquiryService;
import com.prostate.order.util.JsonUtil;
import com.prostate.order.util.UUIDTool;
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
                *    @Description: 创建订单
                *    @Date:  14:29  2018/7/18
                *    @Param:   * @param null
                */
            
    @PostMapping(value = "insert")
    public Map insert(@Validated({GroupA.class,GroupB.class}) OrderInquiry orderInquiry){
        orderInquiry.setId(UUIDTool.getUUID());
        //设置订单状态
        orderInquiry.setOrderStatus("0");
        System.out.println(orderInquiry.getId());
       int result= orderInquiryService.insertSelective(orderInquiry);
        if(result>=0){
            return insertSuccseeResponse(null);
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
                order.setDoctorResponse("内心等待医生回复");
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
    @GetMapping(value = "getOrderById")
    public Map getOrderById(@RequestParam String id){
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
             *    @Description:  订单修改
             *    @Date:  17:55  2018/7/17
             *    @Params:   * @param null
             */
     @PostMapping(value = "updateOrder")
    public  Map updateOrder(@Validated({GroupB.class}) OrderInquiry orderInquiry){
         if (orderInquiry==null||orderInquiry.equals("")||orderInquiry.getId()==null||orderInquiry.getId().equals("")){
             return emptyParamResponse();
         }
         //需要验证一下传过来的id  医生id 患者id 是否正确
         OrderInquiry inquiry = orderInquiryService.selectById(orderInquiry.getId());
         if (inquiry==null||inquiry.equals("")){
             return queryEmptyResponse();
         }else {
            if (inquiry.getDoctorId().equals(orderInquiry.getDoctorId())&&
                    inquiry.getPatientId().equals(orderInquiry.getPatientId())){

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
                //System.out.println("orderInquiry实体"+orderInquiry);
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
