package com.prostate.order.controller;

import com.alibaba.fastjson.JSON;
import com.prostate.order.cache.redis.RedisSerive;
import com.prostate.order.entity.*;
import com.prostate.order.feignService.DoctorServer;
import com.prostate.order.feignService.RecordServer;
import com.prostate.order.feignService.ThirdServer;
import com.prostate.order.feignService.WalletServer;
import com.prostate.order.service.OrderInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private OrderInquiryService orderInquiryService;
    private WalletServer walletServer;
    private RecordServer recordServer;
    private ThirdServer thirdServer;
    private RedisSerive redisSerive;
    private DoctorServer doctorServer;

    //,DoctorServer doctorServer
    @Autowired    //构造器注入
    public OrderInquiryController(OrderInquiryService orderInquiryService,
                                  WalletServer walletServer,
                                  RecordServer recordServer,
                                  ThirdServer thirdServer,
                                  RedisSerive redisSerive,
                                  DoctorServer doctorServer) {
        this.orderInquiryService = orderInquiryService;
        this.walletServer = walletServer;
        this.recordServer = recordServer;
        this.thirdServer = thirdServer;
        this.redisSerive = redisSerive;
        this.doctorServer = doctorServer;
    }


    /**
     * @description: 创建订单
     * @date: 16:52  2018/7/23
     * @param: * @param orderInquiry
     */

    @PostMapping(value = "insert")
    public Map insert(@Validated({GroupOutId.class}) OrderInquiry orderInquiry,
                      String token) {
        orderInquiry.setCreateUser(token);
        //判断患者是否存在
        Map patientDetailResult = recordServer.getPatientDetailById(token);
        log.info("判断患者是否存在" + patientDetailResult.toString());
        if (patientDetailResult.get("code").equals("20000")) {
            String jsonString = JSON.toJSONString(patientDetailResult.get("result"));
            PatientBean patientBean = JSON.parseObject(jsonString, PatientBean.class);
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(currentTime);
            //发短信通知患者  发送 付款成功短信
            Map sendPaymentSuccessResult = thirdServer.sendPaymentSuccess(patientBean.getPatientNumber().substring(3), dateString);
            log.info("发送 付款成功短信" + sendPaymentSuccessResult.toString());
        } else {
            return requestCustomResponse("该患者不存在", null);
        }
        int result = orderInquiryService.insertSelective(orderInquiry);
        if (result > 0) {
            return insertSuccseeResponse(null);
        }
        //TODO 创建订单完成,扣钱操作


        return insertFailedResponse();
    }


    /**
     * @description: 微信用户查询订单 (这里包括已完成,为完成的订单)
     * @date: 16:53  2018/7/23
     * @param: * @param null
     */

    @PostMapping(value = "listByWechatUser")
    public Map listByWechatUser(String token) {
        if (token == null || token.equals("")) {
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry = new OrderInquiry();
        orderInquiry.setPatientId(token);
        List<OrderInquiry> orderInquiries = orderInquiryService.selectByParams(orderInquiry);
        //如果未完成订单,患者端看不到医生编辑的内容
        for (OrderInquiry order : orderInquiries) {
            if (order.getOrderStatus().equals("00") ||
                    order.getOrderStatus().equals("01")) {
                //患者端查询
                order.setDoctorResponse("内心等待医生回复");
            }
        }
        if (orderInquiries.isEmpty() == true) {
            return queryEmptyResponse();
        } else {
            return querySuccessResponse(orderInquiries);
        }
    }


    /**
     * @description: 医生端查询未完成订单
     * @date: 16:53  2018/7/23
     * @param: * @param null
     */

    @PostMapping(value = "listByDoctorUnfinished")
    public Map listByDoctorUnfinished(String token) {
        if (token == null || token.equals("")) {
            return emptyParamResponse();
        }
        //查询来自网络订单
        OrderInquiry orderInquiry = new OrderInquiry();
        orderInquiry.setDoctorId(token);
        orderInquiry.setOrderStatus("00");
        List<OrderInquiry> ordersFromInternet = orderInquiryService.selectByParams(orderInquiry);
        //查询转诊订单
        OrderInquiry inquiry = new OrderInquiry();
        orderInquiry.setDoctorId(token);
        orderInquiry.setOrderStatus("01");
        List<OrderInquiry> ordersFromTransferingConsultation = orderInquiryService.selectByParams(orderInquiry);
        ordersFromInternet.addAll(ordersFromTransferingConsultation);
        if (ordersFromInternet.isEmpty() == true) {
            return queryEmptyResponse();
        } else {
            return querySuccessResponse(ordersFromInternet);
        }
    }


    /**
     * @description: 医生端 查询已完成订单
     * @date: 16:53  2018/7/23
     * @param: * @param token
     */

    @PostMapping(value = "listByDoctorFinished")
    public Map listByDoctorFinished(String token) {
        if (token == null || token.equals("")) {
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry = new OrderInquiry();
        orderInquiry.setDoctorId(token);
        orderInquiry.setOrderStatus("1");
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);
        if (orderInquiryList.isEmpty() == true) {
            return queryEmptyResponse();
        } else {
            return querySuccessResponse(orderInquiryList);
        }
    }


    /**
     * @description: ID查询订单详情
     * @date: 16:54  2018/7/23
     * @param: * @param id
     */

    @GetMapping(value = "getOrderById")
    public Map getOrderById(String id) {
        if (id == null || id.equals("")) {
            return emptyParamResponse();
        }
        OrderInquiry orderInquiry = orderInquiryService.selectById(id);
        if (orderInquiry == null || orderInquiry.equals("")) {
            return queryEmptyResponse();
        } else {
            return querySuccessResponse(orderInquiry);
        }

    }


    /**
     * @description: 医生端订单修改
     * @date: 16:54  2018/7/23
     * @param: * @param null
     */

    @PostMapping(value = "updateOrder")
    public Map updateOrder(@Validated({GroupId.class, GroupOutId.class}) OrderInquiry orderInquiry, String token) {
        //需要验证一下传过来的id  医生id 患者id 是否正确
        OrderInquiry inquiry = orderInquiryService.selectById(orderInquiry.getId());
        if (inquiry == null || inquiry.equals("")) {
            return queryEmptyResponse();
        } else {
            //判断订单状态  如果不是  00  或者  01  医生不能修改订单状态
            if (inquiry.getDoctorId().equals(orderInquiry.getDoctorId()) &&
                    !inquiry.getOrderStatus().equals("1") &&
                    !inquiry.getOrderStatus().equals("2")) {
                //订单价格不可变
                orderInquiry.setOrderPrice(inquiry.getOrderPrice());
                orderInquiry.setUpdateTime(new Date());
                orderInquiry.setUpdateUser(orderInquiry.getDoctorId());
            } else {
                return queryEmptyResponse();
            }
        }
        int result = orderInquiryService.updateSelective(orderInquiry);
        if (result > 0) {
            //这里做个判断,如果orderInquiry的状态  为已完成,需要调用钱包服务
            if (orderInquiry.getOrderStatus().equals("1")) {
                DoctorWallet doctorWallet = new DoctorWallet();
                //查询钱包id
                Map map = walletServer.selectByToken(orderInquiry.getDoctorId());
                log.info("查询钱包id" + map.toString());
                if (map.get("code").equals("40004")) {
                    return requestCustomResponse("该医生没有创建钱包", null);
                } else if (!map.get("code").equals("20000")) {
                    return map;
                }
                String jsonString = JSON.toJSONString(map.get("result"));
                DoctorWallet wallet = JSON.parseObject(jsonString, DoctorWallet.class);
                doctorWallet.setId(wallet.getId());
                doctorWallet.setDoctorId(orderInquiry.getDoctorId());
                doctorWallet.setWalletBalance(orderInquiry.getOrderPrice());

                //调用修改钱包的服务
                Map m = walletServer.updateBalance(doctorWallet, token);
                log.info("调用修改钱包的服务" + m.toString());
                //添加交易记录
                ReceiptPayment receiptPayment = new ReceiptPayment();
                receiptPayment.setWalletId(wallet.getId());
                //医生钱包增加
                receiptPayment.setReceiptPaymentType("支付");
                receiptPayment.setTransactionAmount(orderInquiry.getOrderPrice());
                receiptPayment.setPaymentType("银行卡");
                //查询患者名字  未做
                receiptPayment.setRemark("患者端" + orderInquiry.getPatientId() + " 支付" + orderInquiry.getOrderPrice());

                //调用增加交易记录的服务
                Map n = walletServer.save(receiptPayment, token);
                log.info("调用增加交易记录的服务" + n.toString());
                Map recordResult = null;
                System.out.println("!!!!" + orderInquiry.getPatientId() + "    " + token);
                if (inquiry.getOrderStatus().equals("00")) {
                    //判断该订单之前状态为 00  来源网络
                    recordResult = recordServer.addUserPatient(orderInquiry.getPatientId(), token, "网络");
                    log.info("record服务调用" + recordResult.toString());
                } else if (inquiry.getOrderStatus().equals("01")) {
                    //判断该订单之前状态为 01  来源转诊
                    recordResult = recordServer.addUserPatient(orderInquiry.getPatientId(), token, "转诊");
                    log.info("record服务调用" + recordResult.toString());
                } else {

                }
                //查询医生手机号
                Doctor doctor = redisSerive.getDoctor(token);
                //调用发短息的服务  问诊结束 发送给医生
                Map sendInquiryEndToDoctorReult = thirdServer.sendInquiryEndToDoctor(doctor.getDoctorPhone());
                log.info("问诊结束 发送给医生" + sendInquiryEndToDoctorReult.toString());
            }

            return updateSuccseeResponse();
        } else {
            return updateFailedResponse();
        }
    }

    /**
     * @description: 生成转诊订单
     * @date: 8:57  2018/7/26
     * @param: * @param null
     */


    @PostMapping(value = "transferingConsultationOrder")
    public Map transferingConsultationOrder(String id, String doctorId, String token) {
        //需要验证一下传过来的id  医生id 患者id 是否正确
        OrderInquiry transferingConsultationOrder = orderInquiryService.selectById(id);
        if (transferingConsultationOrder == null || transferingConsultationOrder.equals("")) {
            return queryEmptyResponse();
        } else if (transferingConsultationOrder.getOrderStatus().equals("1") ||
                transferingConsultationOrder.getOrderStatus().equals("2")) {
            return requestCustomResponse("此订单状态不可改变", null);
        } else {
            //查询医生是否存在
            Map doctorResult = doctorServer.getDoctorDetailById(doctorId);
            log.info("查询医生" + doctorResult.toString());
            if (!doctorResult.get("code").equals("20000")) {
                return requestCustomResponse("医生不存在", null);
            }
            transferingConsultationOrder.setOrderStatus("01");
            transferingConsultationOrder.setDoctorId(doctorId);
            transferingConsultationOrder.setUpdateUser(token);
            int result = orderInquiryService.updateSelective(transferingConsultationOrder);
            if (result > 0) {
                return updateSuccseeResponse();
            } else {
                return updateFailedResponse();
            }
        }
    }

}
