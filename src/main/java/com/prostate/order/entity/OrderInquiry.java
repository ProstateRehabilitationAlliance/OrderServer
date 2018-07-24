package com.prostate.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@ToString
public class OrderInquiry extends BaseEntity {
    @NotEmpty(groups = {GroupId.class})
    private String id;

    @NotEmpty(groups = {GroupOutId.class})
    private String patientId;
    @NotEmpty(groups = {GroupOutId.class})
    private String doctorId;

    private String problemDescription;

    private String fileUrl;
    //@NotEmpty(groups = {GroupOutId.class})
    private String orderPrice;
    //@NotEmpty(groups = {GroupOutId.class})
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