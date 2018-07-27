package com.prostate.order.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: developerxiaofeng
 * @Description:
 * @Date: Created in 14:18 2018/7/27
 */
@Getter
@Setter
@ToString
public class OrderInquiryDetailBean {
    //订单id
    private String id;
    //患者名字
    private String patientName;
    //患者年龄
    private String patientAge;
    //患者性别
    private String patientSex;
    //问题描述
    private String problemDescription;
    //订单价格价格
    private String orderPrice;
    //订单状态
    private String orderStatus;
    //身份证号
    private String patientCard;
    //订单日期
    private Date createTime;
    //图片 附件
    private String fileUrl;
    //医生回复
    private String doctorResponse;
}
