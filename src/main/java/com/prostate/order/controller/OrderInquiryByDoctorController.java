package com.prostate.order.controller;

import com.alibaba.fastjson.JSON;
import com.prostate.order.beans.OrderInquiryListBean;
import com.prostate.order.cache.redis.RedisSerive;
import com.prostate.order.entity.*;
import com.prostate.order.feignService.DoctorServer;
import com.prostate.order.feignService.RecordServer;
import com.prostate.order.feignService.ThirdServer;
import com.prostate.order.feignService.WalletServer;
import com.prostate.order.service.OrderInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: developerxiaofeng
 * @Description: 医生app端  对订单的操作
 * @Date: Created in 11:25 2018/7/27
 */
@Slf4j
@RestController
@RequestMapping(value = "orderInquiryByDoctor")
public class OrderInquiryByDoctorController extends BaseController {
    private OrderInquiryService orderInquiryService;
    //钱包服务
    private WalletServer walletServer;
    private RecordServer recordServer;
    //第三方服务
    private ThirdServer thirdServer;
    private RedisSerive redisSerive;
    //医生服务
    private DoctorServer doctorServer;

    //,DoctorServer doctorServer
    @Autowired    //构造器注入
    public OrderInquiryByDoctorController(OrderInquiryService orderInquiryService,
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
     * @description: 医生端查询 问诊订单列表(未完成)
     * @date: 11:03  2018/7/27
     * @param: * @param null
     */

    @PostMapping(value = "getOrderInquiryListUnfinish")
    public Map getOrderInquiryListUnfinish(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setDoctorId(token);
        System.out.println("--->" + orderInquiry);
        //订单状态为 未完成
        orderInquiry.setOrderStatus(OrderConstants.TO_BE_ANSWERED);
        //问诊类型为 网络
        orderInquiry.setOrderType(OrderConstants.INQUIRY_TYPE);
        //调用select 服务 向数据库插入数据
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);
        //数据插入结果 校验
        if (orderInquiryList.isEmpty()) {
            return queryEmptyResponse();
        }
        List<OrderInquiryListBean> orderInquiryListBeanList = orderListBuilder(orderInquiryList);

        return querySuccessResponse(orderInquiryListBeanList);
    }

    /**
     * @description: 医生端查询 转诊订单  (未完成)
     * @date: 11:04  2018/7/27
     * @param: * @param null
     */

    @PostMapping(value = "getOrderReferralList")
    public Map getOrderReferralList(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setDoctorId(token);
        //订单状态为 未完成
        orderInquiry.setOrderStatus(OrderConstants.TO_BE_ANSWERED);
        //问诊类型为 网络
        orderInquiry.setOrderType(OrderConstants.REFERRAL_TYPE);
        //调用select 服务 向数据库插入数据
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);
        //数据插入结果 校验
        if (orderInquiryList.isEmpty()) {
            return queryEmptyResponse();
        }
        List<OrderInquiryListBean> orderInquiryListBeanList = orderListBuilder(orderInquiryList);

        return querySuccessResponse(orderInquiryListBeanList);
    }

    /**
     * @description: 医生端查询 订单列表(已完成)
     * @date: 11:03  2018/7/27
     * @param: * @param null
     */

    @PostMapping(value = "getOrderInquiryListFinish")
    public Map getOrderInquiryListFinish(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setDoctorId(token);
        //订单状态为 未完成
        orderInquiry.setOrderStatus(OrderConstants.DONE);
        //调用select 服务 向数据库插入数据
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);
        //数据插入结果 校验
        if (orderInquiryList.isEmpty()) {
            return queryEmptyResponse();
        }
        List<OrderInquiryListBean> orderInquiryListBeanList = orderListBuilder(orderInquiryList);

        return querySuccessResponse(orderInquiryListBeanList);
    }

    /**
     * @description: 订单 需要补充检查资料
     * @date: 8:57  2018/7/26
     * @param: * @param null
     */
    @PostMapping(value = "orderAddInfomation")
    public Map orderAddInfomation(String id, String token) {
        //通过id查询订单
        OrderInquiry inquiry = orderInquiryService.selectById(id);
        if (inquiry == null) {
            return queryEmptyResponse();
        }
        //判断订单状态
        if (!inquiry.getOrderStatus().equals("ANSWERED")) {
            return requestCustomResponse("此订单不是待回复状态", null);
        }
        inquiry.setOrderStatus("ADD");
        int result = orderInquiryService.updateSelective(inquiry);
        if (result > 0) {
            return updateSuccseeResponse();
        }
        return updateFailedResponse();

    }

    /**
     * @description: 订单 回复 已完成
     * @date: 8:57  2018/7/26
     * @param: * @param null
     */

    @PostMapping(value = "orderDone")
    public Map orderDone(String id, String doctorResponse, String token) {
        //通过id查询订单
        OrderInquiry inquiry = orderInquiryService.selectById(id);
        if (inquiry == null) {
            return queryEmptyResponse();
        }
        //判断订单状态
        if (!inquiry.getOrderStatus().equals("ANSWERED")) {
            return requestCustomResponse("此订单不是待回复状态", null);
        }
        inquiry.setOrderStatus("DONE");
        inquiry.setDoctorResponse(doctorResponse);
        inquiry.setUpdateUser(token);
        int result = orderInquiryService.updateSelective(inquiry);
        if (result > 0) {
            //这里做个判断,如果orderInquiry的状态  为已完成,需要调用钱包服务
            DoctorWallet doctorWallet = new DoctorWallet();
            //查询钱包id
            Map map = walletServer.selectByToken(inquiry.getDoctorId());
            log.info("查询钱包id" + map.toString());
            if (map.get("code").equals("40004")) {
                return requestCustomResponse("该医生没有创建钱包", null);
            } else if (!map.get("code").equals("20000")) {
                return map;
            }
            String jsonString = JSON.toJSONString(map.get("result"));
            DoctorWallet wallet = JSON.parseObject(jsonString, DoctorWallet.class);
            doctorWallet.setId(wallet.getId());
            doctorWallet.setDoctorId(inquiry.getDoctorId());
            doctorWallet.setWalletBalance(inquiry.getOrderPrice());

            //调用修改钱包的服务
            Map m = walletServer.updateBalance(doctorWallet, token);
            log.info("调用修改钱包的服务" + m.toString());
            //添加交易记录
            ReceiptPayment receiptPayment = new ReceiptPayment();
            receiptPayment.setWalletId(wallet.getId());
            //医生钱包增加
            receiptPayment.setReceiptPaymentType("支付");
            receiptPayment.setTransactionAmount(inquiry.getOrderPrice());
            receiptPayment.setPaymentType("银行卡");
            //查询患者名字  未做
            receiptPayment.setRemark("患者端" + inquiry.getPatientId() + " 支付" + inquiry.getOrderPrice());

            //调用增加交易记录的服务
            Map n = walletServer.save(receiptPayment, token);
            log.info("调用增加交易记录的服务" + n.toString());
            Map recordResult = null;
            System.out.println("!!!!" + inquiry.getPatientId() + "    " + token);
            if (inquiry.getOrderStatus().equals("00")) {
                //判断该订单之前状态为 00  来源网络
                recordResult = recordServer.addUserPatient(inquiry.getPatientId(), token, "网络");
                log.info("record服务调用" + recordResult.toString());
            } else if (inquiry.getOrderStatus().equals("01")) {
                //判断该订单之前状态为 01  来源转诊
                recordResult = recordServer.addUserPatient(inquiry.getPatientId(), token, "转诊");
                log.info("record服务调用" + recordResult.toString());
            } else {

            }
            //查询医生手机号
            Doctor doctor = redisSerive.getDoctor(token);
            //调用发短息的服务  问诊结束 发送给医生
            Map sendInquiryEndToDoctorReult = thirdServer.sendInquiryEndToDoctor(doctor.getDoctorPhone());
            log.info("问诊结束 发送给医生" + sendInquiryEndToDoctorReult.toString());

            return updateSuccseeResponse();
        } else {
            return updateFailedResponse();
        }

    }

    /**
     * @description: 订单 存为草稿
     * @date: 8:57  2018/7/26
     * @param: * @param null
     */
    @PostMapping(value = "orderToDraft")
    public Map orderToDraft(String id, String doctorResponse, String token) {
        //通过id查询订单
        OrderInquiry inquiry = orderInquiryService.selectById(id);
        if (inquiry == null) {
            return queryEmptyResponse();
        }
        //判断订单状态  只有待回复状态才能存为草稿
        if (!inquiry.getOrderStatus().equals("ANSWERED")) {
            return requestCustomResponse("此订单不是待回复状态", null);
        }
        //订单状态还是待回复状态
        inquiry.setDoctorResponse(doctorResponse);
        inquiry.setUpdateUser(token);
        inquiry.setUpdateTime(new Date());
        int result = orderInquiryService.updateSelective(inquiry);
        if (result > 0) {
            return updateSuccseeResponse();
        }
        return updateFailedResponse();

    }

    /**
     * @description: 生成 转诊订单
     * @date: 8:57  2018/7/26
     * @param: * @param null
     */


    @PostMapping(value = "transferingConsultationOrder")
    public Map transferingConsultationOrder(String id, String doctorId, String token) {
        //判断医生是否存在
//        Map doctorResult = doctorServer.getDoctorDetailById(doctorId);
//        log.info("查询医生" + doctorResult.toString());
//        if (!doctorResult.get("code").equals("20000")) {
//            return requestCustomResponse("订单中医生不存在", null);
//        }
        //需要验证一下传过来的id  医生id 患者id 是否正确
        OrderInquiry transferOrder = orderInquiryService.selectById(id);
        if (transferOrder == null) {
            return queryEmptyResponse();
        }
        //判断订单状态  只有待回复状态才能存为草稿
        if (!transferOrder.getOrderStatus().equals("ANSWERED")) {
            return requestCustomResponse("此订单不是待回复状态", null);
        }
        transferOrder.setOrderType(OrderConstants.REFERRAL_TYPE);
        transferOrder.setDoctorId(doctorId);
        transferOrder.setUpdateUser(token);
        int result = orderInquiryService.updateSelective(transferOrder);
        if (result > 0) {
            return updateSuccseeResponse();
        } else {
            return updateFailedResponse();
        }

    }


    /**
     * @description: 组装 订单列表信息
     * @date: 17:43  2018/7/27
     * @param: * @param null
     */

    private List<OrderInquiryListBean> orderListBuilder(List<OrderInquiry> orderInquiryList) {
        List<OrderInquiryListBean> orderInquiryListBeanList = new ArrayList<>();
        for (OrderInquiry inquiry : orderInquiryList) {
            Map patientBeanResult = recordServer.getPatientDetailById(inquiry.getPatientId());
            OrderInquiryListBean orderInquiryListBean = new OrderInquiryListBean();
            log.info("判断就诊人是否存在" + patientBeanResult.toString());
            String jsonString = JSON.toJSONString(patientBeanResult.get("result"));
            PatientBean patientBean = JSON.parseObject(jsonString, PatientBean.class);
            //订单id
            orderInquiryListBean.setId(inquiry.getId());
            //患者名字
            orderInquiryListBean.setPatientName(patientBean.getPatientName());
            //患者年龄
            orderInquiryListBean.setPatientAge(patientBean.getPatientAge());
            //患者性别
            orderInquiryListBean.setPatientSex(patientBean.getPatientSex());
            //问题描述
            orderInquiryListBean.setProblemDescription(inquiry.getProblemDescription());
            //订单价格
            orderInquiryListBean.setOrderPrice(inquiry.getOrderPrice());
            //订单状态
            orderInquiryListBean.setOrderStatus(inquiry.getOrderStatus());
            //订单日期
            orderInquiryListBean.setCreateTime(inquiry.getCreateTime());
            orderInquiryListBeanList.add(orderInquiryListBean);
        }
        return orderInquiryListBeanList;
    }


}
