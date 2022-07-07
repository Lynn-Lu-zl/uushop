package com.project001.controller;

import com.project001.exception.Assert;
import com.project001.result.ResponseEnum;
import com.project001.util.RegexValidateUtil;
import com.project001.util.ResultVOUtil;
import com.project001.vo.ResultVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project001.service.SmsService;
import com.project001.util.RandomUtil;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Resource
    private SmsService smsService;
    @Resource
    private RedisTemplate redisTemplate;



    @GetMapping("/send/{mobile}")
    public ResultVO send(@PathVariable("mobile") String mobile)
    {
        //验证手机号不能为空
        Assert.notNull(mobile,ResponseEnum.PARAMETER_NULL);
        //验证手机号格式
        Assert.isTrue(RegexValidateUtil.checkMobile(mobile),ResponseEnum.MOBILE_ERROR);
        //随机生成6位验证码
        String code = RandomUtil.getSixBitRandom();
        boolean send = smsService.send(mobile, code);
        if (send)
        {
            //todo 在redis新增一个字符串类型的值，在UserController中是opsForValue().get
            this.redisTemplate.opsForValue().set("uushop-sms-code-"+mobile, code);
            //{未充值状态码code=10010, charge=false, remain=0, msg=接口需要付费，请充值, requestId=d45fd8fad6344f6fa35cb98a1a4706e7}
            return ResultVOUtil.success("短信发送成功");
        }
        return ResultVOUtil.fail("短信发送失败");
    }
}
