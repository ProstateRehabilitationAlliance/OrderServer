package com.prostate.order.service.impl;

import com.prostate.order.entity.OrderInquiry;
import com.prostate.order.mapper.master.OrderInquiryWriteMapper;
import com.prostate.order.mapper.slave.OrderInquiryReadMapper;
import com.prostate.order.service.OrderInquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 13:23 2018/7/17
 */
@Service
public class OrderInquiryServiceImpl implements OrderInquiryService {
    @Autowired
    private OrderInquiryReadMapper orderInquiryReadMapper;
    @Autowired
    private OrderInquiryWriteMapper orderInquiryWriteMapper;


    @Override
    public int insertSelective(OrderInquiry orderInquiry) {
        return orderInquiryWriteMapper.insertSelective(orderInquiry);
    }

    @Override
    public int updateSelective(OrderInquiry orderInquiry) {
        return orderInquiryWriteMapper.updateByPrimaryKeySelective(orderInquiry);
    }

    @Override
    public OrderInquiry selectById(String id) {
        return orderInquiryReadMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrderInquiry> selectByParams(OrderInquiry orderInquiry) {
        return orderInquiryReadMapper.selectByParams(orderInquiry);
    }

    @Override
    public int deleteById(String id) {
        return 0;
    }
}
