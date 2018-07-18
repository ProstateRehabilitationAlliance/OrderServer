package com.prostate.order.mapper.master;

import com.prostate.order.entity.OrderInquiry;
import org.springframework.stereotype.Repository;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 13:28 2018/7/17
 */
@Repository
public interface OrderInquiryWriteMapper extends BaseWriteMapper<OrderInquiry> {


    int updateByPrimaryKeySelective(OrderInquiry e);
}
