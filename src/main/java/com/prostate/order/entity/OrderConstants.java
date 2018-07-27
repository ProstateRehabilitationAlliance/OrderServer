package com.prostate.order.entity;

public class OrderConstants {

    //订单状态
    public final static String PENDING_PAYMENT = "PAYMENT"; //待支付
    public final static String TO_BE_ADD = "ADD"; //需要补充资料
    public final static String TO_BE_ANSWERED = "ANSWERED"; //待回复
    public final static String DONE = "DONE";   //已完成

    ///订单类型
    public final static String INQUIRY_TYPE = "INQUIRY";   //问诊订单
    public final static String REFERRAL_TYPE = "REFERRAL";   //转诊订单


}
