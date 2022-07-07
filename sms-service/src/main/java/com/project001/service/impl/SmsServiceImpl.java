package com.project001.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.project001.exception.ShopException;
import com.project001.result.ResponseEnum;
import com.project001.service.SmsService;
import com.wxapi.WxApiCall.WxApiCall;
import com.wxapi.model.RequestModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public boolean send(String mobile, String code) {
        RequestModel model = new RequestModel();

        // model.setAppkey(SmsUtil.Appkey);
        // model.setGwUrl(SmsUtil.Url);

            model.setGwUrl("https://way.jd.com/chuangxin/dxjk");
            model.setAppkey("91b467b31cd11ca773fae120f3327594");
        Map querymap = new HashMap<>();
        //访问参数，自定义的密钥sign: UUShop
        // querymap.put("sign",SmsUtil.Sign);
        querymap.put("sign","UUShop");
        //访问参数
        querymap.put("mobile",mobile);
        //访问参数
        querymap.put("content", "您本次的验证码是："+code);
        model.setQueryParams(querymap);

        try {
            WxApiCall call = new WxApiCall();
            call.setModel(model);
            call.request();
            String request = call.request();
            Gson gson = new Gson();
            Map<String,String> map = gson.fromJson(request,
                    new TypeToken<Map<String,String>>(){}.getType());
            System.out.println(map);
            if(map.get("code").equals("10010"))return true;
        } catch (JsonSyntaxException e) {
            throw new ShopException(ResponseEnum.SMS_SEND_ERROR.getMsg());
        }

        return false;
    }

    // public boolean send(String mobile, String code) {
    //     RequestModel model = new RequestModel();
    //     model.setGwUrl("https://way.jd.com/chuangxin/dxjk");
    //     model.setAppkey("91b467b31cd11ca773fae120f3327594");
    //     Map queryMap = new HashMap();
    //     queryMap.put("mobile",mobile); //访问参数
    //     queryMap.put("sign",SmsUtil.Sign);
    //     queryMap.put("content","【uushop】你的验证码是："+ code +"，3分钟内有效！"); //访问参数
    //     model.setQueryParams(queryMap);
    //     WxApiCall call = new WxApiCall();
    //     call.setModel(model);
    //     call.request();
    //     return false;


    // }
}
