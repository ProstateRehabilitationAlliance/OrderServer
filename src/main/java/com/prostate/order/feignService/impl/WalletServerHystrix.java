package com.prostate.order.feignService.impl;

import com.prostate.order.entity.DoctorWallet;
import com.prostate.order.entity.ReceiptPayment;
import com.prostate.order.feignService.WalletServer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 9:23 2018/7/19
 */
@Component
public class WalletServerHystrix extends BaseServerHystrix implements WalletServer {
    /**
     *    @Description:   修改钱包金额
     *    @Date:  9:28  2018/7/19
     *    @Param:   * @param doctorWallet
     */
    @Override
    public Map updateBalance(DoctorWallet doctorWallet) {
        return resultMap;
    }
    /**
     *    @Description:添加交易记录
     *    @Date:  9:31  2018/7/19
     *    @Param:   * @param receiptPayment
     */
    @Override
    public Map save(ReceiptPayment receiptPayment) {
        return resultMap;
    }
   /**
       *    @Description:  根据医生id查询钱包信息
       *    @Date:  9:59  2018/7/19
       *    @Param:   * @param null
       */

    @Override
    public Map selectByDoctorId(String doctorId) {
        return resultMap;
    }
}