package com.prostate.order.util;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import java.io.IOException;

/**
 * @Author: developerfengrui
 * @Description:    短信服务
 * @Date: Created in 11:14 2018/7/18
 */
public class SendMessageUtil {

    public static void main(String[] args) {
        String [] phone ={"18435138713"};
        send(1400114511,"15c70ba3ca084c5fc92950eb7851598a7",
               phone,158220 ,"haha");
    }

    public  static void send(int appid,String appkey,
                             String []phoneNumbers,int templateId,String smsSign){
        try {
            String[] params = {"wawa","100"};
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }
}
