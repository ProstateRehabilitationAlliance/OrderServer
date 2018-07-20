package com.prostate.order.util;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: developerfengrui
 * @Description:    短信服务
 * @Date: Created in 11:14 2018/7/18
 */
public class SendMessageUtil {

    public static void main(String[] args) {
        String [] phone ={"18435138713"};
        String[] params = {"wawa","2"};
        send(1400114511,"5c70ba3ca084c5fc92950eb7851598a7",
               phone,158220 ,"haha",params);
    }

    /**
     *    @Description:   【Java程序员技术分享】[haha]wawa为您的登录验证码，请于100分钟内填写。如非本人操作，请忽略本短信。
     *    @Date:  14:37  2018/7/19
     *    @Param:
     * @param appid         例 1400114511
     * @param appkey        例  5c70ba3ca084c5fc92950eb7851598a7
     * @param phoneNumbers  电话号码数组  String [] phone ={"18435138713"};
     * @param templateId    例   158220
     * @param smsSign   例 haha
     *  @param params      例     {"wawa","2"};
     */
    public  static String send(int appid, String appkey,
                            String []phoneNumbers, int templateId, String smsSign, String[] params){

        SmsSingleSenderResult result=null;
        try {

            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            result= ssender.sendWithParam("86", phoneNumbers[0],
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
        return result.toString();
    }
}
