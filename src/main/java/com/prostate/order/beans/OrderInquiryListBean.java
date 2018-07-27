package com.prostate.order.beans;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderInquiryListBean {

    private String patientName;
    private String patientAge;
    private String patientSex;

    private String problemDescription;

    private String orderPrice;

}
