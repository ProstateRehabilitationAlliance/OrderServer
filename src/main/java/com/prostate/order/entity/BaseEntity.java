package com.prostate.order.entity;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BaseEntity{

    public final static int PAGE_SIZE = 100;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int pageSize;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int pageNo;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

}
