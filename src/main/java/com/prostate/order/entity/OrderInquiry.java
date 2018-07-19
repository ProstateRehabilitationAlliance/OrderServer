package com.prostate.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.Date;
@Getter
@Setter
@ToString
public class OrderInquiry extends BaseEntity{
   @Null(groups = {GroupA.class})
    private String id;

    @NotEmpty(groups = {GroupB.class})
    private String patientId;
    @NotEmpty(groups = {GroupB.class})
    private String doctorId;
    @NotEmpty(groups = {GroupB.class})
    private String problemDescription;

    private String fileUrl;
    @NotEmpty(groups = {GroupB.class})
    private String orderPrice;

    private String doctorResponse;

    private String orderStatus;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Date deleteTime;

    private String deleteUser;

    private String delFlag;


}