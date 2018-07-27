package com.prostate.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.prostate.order.beans.OrderInquiryListBean;
import com.prostate.order.cache.redis.RedisSerive;
import com.prostate.order.entity.GroupOutId;
import com.prostate.order.entity.OrderInquiry;
import com.prostate.order.feignService.DoctorServer;
import com.prostate.order.feignService.RecordServer;
import com.prostate.order.feignService.ThirdServer;
import com.prostate.order.feignService.WalletServer;
import com.prostate.order.service.OrderInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "OrderInquiry2")
public class MaxCoderController extends BaseController {


    private final OrderInquiryService orderInquiryService;
    private final WalletServer walletServer;
    private final RecordServer recordServer;
    private final ThirdServer thirdServer;
    private final RedisSerive redisSerive;
    private final DoctorServer doctorServer;

    @Autowired
    public MaxCoderController(OrderInquiryService orderInquiryService, WalletServer walletServer, RecordServer recordServer, ThirdServer thirdServer, RedisSerive redisSerive, DoctorServer doctorServer) {
        this.orderInquiryService = orderInquiryService;
        this.walletServer = walletServer;
        this.recordServer = recordServer;
        this.thirdServer = thirdServer;
        this.redisSerive = redisSerive;
        this.doctorServer = doctorServer;
    }


    /**
     * 患者 创建订单
     */
    @PostMapping(value = "createOrder")
    public Map createOrder(@Validated({GroupOutId.class}) OrderInquiry orderInquiry, String token) {
        //数据插入对象 赋值
        orderInquiry.setCreateUser(token);
        //调用insert 服务 向数据库插入数据
        int result = orderInquiryService.insertSelective(orderInquiry);
        //数据插入结果 校验
        if (result > 0) {
            return insertSuccseeResponse(orderInquiry);
        }
        return insertFailedResponse();
    }

    /**
     * 信用户 查询订单列表
     */
    @PostMapping(value = "getOrderList")
    public Map getOrderList(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setCreateUser(token);
        //调用insert 服务 向数据库插入数据
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);
        //数据插入结果 校验
        if (orderInquiryList.isEmpty()) {
            return queryEmptyResponse();
        }
        List<OrderInquiryListBean> orderInquiryListBeanList = orderListBuilder(orderInquiryList);

        return querySuccessResponse(orderInquiryListBeanList);
    }


    /**
     * 信用户 查询订单详情
     */
    @PostMapping(value = "getOrderDetail")
    public Map getOrderDetail(String orderId) {
        //调用insert 服务 向数据库插入数据
        OrderInquiry orderInquiry = orderInquiryService.selectById(orderId);
        //数据插入结果 校验
        if (orderInquiry == null) {
            return queryEmptyResponse();
        }
        Map<String, Object> patientMap = recordServer.getPatientDetailById(orderInquiry.getPatientId());
        JSONObject jsonObject = (JSONObject) patientMap.get("result");
        return querySuccessResponse(orderInquiry);
    }

    /**
     * 医生端查询 问诊订单
     */
    @PostMapping(value = "getOrderInquiryList")
    public Map getOrderInquiryList(String token) {
        OrderInquiry orderInquiry = new OrderInquiry();
        orderInquiry.setDoctorId(token);
        orderInquiry.setOrderStatus("00");
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);

        if (orderInquiryList.isEmpty()) {
            return queryEmptyResponse();
        } else {
            return querySuccessResponse(orderInquiryList);
        }
    }

    /**
     * 医生端查询 转诊订单
     */
    @PostMapping(value = "getOrderReferralList")
    public Map getOrderReferralList(String token) {
        OrderInquiry orderInquiry = new OrderInquiry();
        orderInquiry.setDoctorId(token);
        orderInquiry.setOrderStatus("01");
        List<OrderInquiry> orderInquiryList = orderInquiryService.selectByParams(orderInquiry);
        if (orderInquiryList.isEmpty()) {
            return queryEmptyResponse();
        } else {
            return querySuccessResponse(orderInquiryList);
        }
    }


    /**
     * 组装 订单列表信息
     *
     * @param orderInquiryList
     * @return
     */
    private List<OrderInquiryListBean> orderListBuilder(List<OrderInquiry> orderInquiryList) {
        List<OrderInquiryListBean> orderInquiryListBeanList = new ArrayList<>();
        for (OrderInquiry inquiry : orderInquiryList) {
            recordServer.getPatientDetailById(inquiry.getPatientId());
            OrderInquiryListBean orderInquiryListBean = new OrderInquiryListBean();

            orderInquiryListBeanList.add(orderInquiryListBean);
        }
        return orderInquiryListBeanList;
    }
}
