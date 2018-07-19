package com.prostate.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 9:06 2018/7/19
 */
@Getter
@Setter
@ToString
public class DoctorWallet extends BaseEntity{

   private String id;

    private String doctorId;

      private String walletBalance;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Date deleteTime;

    private String deleteUser;

    private String delFlag;


}
