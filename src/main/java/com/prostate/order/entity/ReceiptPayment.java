package com.prostate.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 9:10 2018/7/19
 */
@Getter
@Setter
@ToString
public class ReceiptPayment extends BaseEntity {

    @NotNull(message = "id不能为空",groups = GroupId.class)
    @Pattern(regexp = "^[A-Za-z0-9]{32}",message = "id必须是32位字符串",groups = GroupId.class)
    private String id;

    @NotNull(message =" 钱包id不能为空" ,groups = {GroupOutId.class})
    @Pattern(regexp = "^[A-Za-z0-9]{32}",message = "钱包id必须是32位字符串",groups = GroupOutId.class)
    private String walletId;

    private String serialNumber;

    @NotNull(message =" 交易类型不能为空" ,groups = {GroupOutId.class})
    private String receiptPaymentType;

    @NotNull(message =" 交易金额不能为空" ,groups = {GroupOutId.class})
    private String transactionAmount;

    @NotNull(message =" 支付方式不能为空" ,groups = {GroupOutId.class})
    private String paymentType;

    private String walletBalance;

    @Size(max = 200,message = "备注内容不能多于100字。",groups = GroupOutId.class)
    private String remark;

    private Date createTime;
}
