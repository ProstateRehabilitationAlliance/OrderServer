package com.prostate.order.feignService;

import com.prostate.order.entity.DoctorWallet;
import com.prostate.order.entity.GroupA;
import com.prostate.order.entity.GroupB;
import com.prostate.order.entity.ReceiptPayment;
import com.prostate.order.feignService.impl.WalletServerHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 9:18 2018/7/19
 */

//
@FeignClient(value = "wallet-server",fallback = WalletServerHystrix.class)
public interface WalletServer {


    @PostMapping(value = "/doctorWallet/updateBalance")
    public Map updateBalance(@RequestBody DoctorWallet doctorWallet);

    @GetMapping("/doctorWallet/selectByDoctorId")
    public Map selectByDoctorId(@RequestParam("doctorId") String doctorId);

    @PostMapping(value = "/receiptPayment/save")
    public Map save(@RequestBody ReceiptPayment receiptPayment);


}
