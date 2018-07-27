package com.prostate.order.controller;

import com.alibaba.fastjson.JSON;
import com.prostate.order.beans.OrderInquiryDetailBean;
import com.prostate.order.beans.OrderInquiryListBean;
import com.prostate.order.cache.redis.RedisSerive;
import com.prostate.order.entity.GroupOutId;
import com.prostate.order.entity.OrderConstants;
import com.prostate.order.entity.OrderInquiry;
import com.prostate.order.entity.PatientBean;
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

/**
 * @Author: developerxiaofeng
 * @Description: 微信公众号端  对订单的操作
 * @Date: Created in 11:25 2018/7/27
 */
@Slf4j
@RestController
@RequestMapping(value = "orderInquiryByWechat")
public class OrderInquiryByWechatController extends BaseController {
    private OrderInquiryService orderInquiryService;
    //钱包服务
    private WalletServer walletServer;
    private RecordServer recordServer;
    //第三方服务
    private ThirdServer thirdServer;
    private RedisSerive redisSerive;
    //医生服务
    private DoctorServer doctorServer;

    @Autowired    //构造器注入
    public OrderInquiryByWechatController(OrderInquiryService orderInquiryService,
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
     * @description: 创建订单  订单创建完成进入待支付状态
     * @date: 16:52  2018/7/23
     * @param: * @param orderInquiry
     */

    @PostMapping(value = "createOrder")
    public Map createOrder(@Validated({GroupOutId.class}) OrderInquiry orderInquiry,
                           String token) {
        /*//判断就诊人是否存在
        Map patientResult = recordServer.getPatientDetailById(orderInquiry.getPatientId());
        log.info("判断就诊人是否存在" + patientResult.toString());
        if (!patientResult.get("code").equals("20000")) {
            return requestCustomResponse("订单中就诊人不存在", null);
        }
        //判断医生是否存在
        Map doctorResult = doctorServer.getDoctorDetailById(orderInquiry.getDoctorId());
        log.info("查询医生" + doctorResult.toString());
        if (!doctorResult.get("code").equals("20000")) {
            return requestCustomResponse("订单中医生不存在", null);
        }*/
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
     * @description: 微信用户查询订单列表 (这里包括已完成,未完成的订单)
     * @date: 16:53  2018/7/23
     * @param: * @param null
     */

    @PostMapping(value = "getOrderListByWechat")
    public Map getOrderListByWechat(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setCreateUser(token);
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
     * @description: 微信端 查询未完成订单
     * @date: 16:53  2018/7/23
     * @param: * @param null
     */

    @PostMapping(value = "listByWechatUnfinished")
    public Map listByWechatUnfinished(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setCreateUser(token);
        //查询条件  为未完成订单
        orderInquiry.setOrderStatus(OrderConstants.TO_BE_ANSWERED);
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
     * @description: 微信 查询已完成订单
     * @date: 16:53  2018/7/23
     * @param: * @param token
     */

    @PostMapping(value = "listByWechatFinished")
    public Map listByWechatFinished(String token) {
        //创建查询条件
        OrderInquiry orderInquiry = new OrderInquiry();
        //数据插入对象 赋值
        orderInquiry.setCreateUser(token);
        //查询条件  为未完成订单
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
     * @description: 微信端 APP端 通用 查询订单详情
     * @date: 10:59  2018/7/27
     * @param: * @param null
     */

    @PostMapping(value = "getOrderDetail")
    public Map getOrderDetail(String id) {
        //调用select 服务 向数据库查询数据
        OrderInquiry orderInquiry = orderInquiryService.selectById(id);
        //数据插入结果 校验
        if (orderInquiry == null) {
            return queryEmptyResponse();
        }
        OrderInquiryDetailBean orderInquiryDetailBean = orderDetailBuilder(orderInquiry);
        return querySuccessResponse(orderInquiryDetailBean);
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
            OrderInquiryListBean orderInquiryListBean = new OrderInquiryListBean();
            Map patientBeanResult = recordServer.getPatientDetailById(inquiry.getPatientId());
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


    /**
     * 组装 订单详细信息
     *
     * @param orderInquiry
     * @return
     */
    private OrderInquiryDetailBean orderDetailBuilder(OrderInquiry orderInquiry) {
        OrderInquiryDetailBean orderInquiryDetailBean = new OrderInquiryDetailBean();
        Map patientBeanResult = recordServer.getPatientDetailById(orderInquiry.getPatientId());
        log.info("判断就诊人是否存在" + patientBeanResult.toString());
        String jsonString = JSON.toJSONString(patientBeanResult.get("result"));
        PatientBean patientBean = JSON.parseObject(jsonString, PatientBean.class);
        //订单id
        orderInquiryDetailBean.setId(orderInquiry.getId());
        //患者名字
        orderInquiryDetailBean.setPatientName(patientBean.getPatientName());
        //患者年龄
        orderInquiryDetailBean.setPatientAge(patientBean.getPatientAge());
        //患者性别
        orderInquiryDetailBean.setPatientSex(patientBean.getPatientSex());
        //问题描述
        orderInquiryDetailBean.setProblemDescription(orderInquiry.getProblemDescription());
        //订单价格
        orderInquiryDetailBean.setOrderPrice(orderInquiry.getOrderPrice());
        //订单状态
        orderInquiryDetailBean.setOrderStatus(orderInquiry.getOrderStatus());
        //患者身份证号
        orderInquiryDetailBean.setPatientCard(patientBean.getPatientCard());
        //订单日期
        orderInquiryDetailBean.setCreateTime(orderInquiry.getCreateTime());
        //附件
        orderInquiryDetailBean.setFileUrl(orderInquiry.getFileUrl());
        //医生回复
        orderInquiryDetailBean.setDoctorResponse(orderInquiry.getDoctorResponse());

        return orderInquiryDetailBean;
    }
}
