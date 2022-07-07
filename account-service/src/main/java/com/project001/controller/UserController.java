package com.project001.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project001.entity.User;
import com.project001.exception.Assert;
import com.project001.exception.ShopException;
import com.project001.form.UserLoginForm;
import com.project001.form.UserRegisterForm;
import com.project001.result.ResponseEnum;
import com.project001.service.UserService;
import com.project001.util.RegexValidateUtil;
import com.project001.util.ResultVOUtil;
import com.project001.utils.JwtUtil;
import com.project001.utils.MD5Util;
import com.project001.vo.ResultVO;
import com.project001.vo.UserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lynn
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    //redis
    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 用户注册
     * @param userRegisterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public ResultVO register(@Valid @RequestBody UserRegisterForm userRegisterForm, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            //抛出异常，用户信息不能为空
            throw new ShopException(ResponseEnum.USER_INFO_NULL.getMsg());
        }
        //判断手机号格式是否正确
        Assert.isTrue(RegexValidateUtil.checkMobile(userRegisterForm.getMobile()), ResponseEnum.MOBILE_ERROR);


        //判断手机号是否已经注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile,userRegisterForm.getMobile());
        User one = userService.getOne(wrapper);
        if (one !=null )
        {
            //抛出异常，手机号已被注册
            throw new ShopException(ResponseEnum.USER_MOBILE_EXIST.getMsg());
        }
        //todo 获取在redis存储短信验证码，在SmsController中是opsForValue().set
        String code = (String) this.redisTemplate.opsForValue().get("uushop-sms-code-" + userRegisterForm.getMobile());
        //判断用户输入的短信验证码是否正确
        Assert.equals(code,userRegisterForm.getCode(),ResponseEnum.USER_CODE_ERROR);

        User user = new User();
        user.setMobile(userRegisterForm.getMobile());
        //MD5加密用户密码
        user.setPassword(MD5Util.getSaltMD5(userRegisterForm.getPassword()));
        boolean save = userService.save(user);
        if(save) {
            return ResultVOUtil.success("注册成功");
        }
        return ResultVOUtil.fail("注册失败");
    }
    /**
     * 用户登录
     * @param userLoginForm
     * @param bindingResult
     * @return
     */
    @GetMapping("/login")
    public ResultVO login(@Valid  UserLoginForm userLoginForm, BindingResult bindingResult){

        if (bindingResult.hasErrors())
        {
            //抛出异常，用户信息不能为空
            throw new ShopException(ResponseEnum.USER_INFO_NULL.getMsg());
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile, userLoginForm.getMobile());
        User one = userService.getOne(wrapper);

        if (one == null)
        {
            //抛出异常，手机号不存在
            throw new ShopException(ResponseEnum.USER_MOBILE_NULL.getMsg());
        }
        //验证
        boolean saltverifyMD5 = MD5Util.getSaltverifyMD5(userLoginForm.getPassword(), one.getPassword());
        if (saltverifyMD5 == false)
        {

            //抛出异常，密码错误
            throw new ShopException(ResponseEnum.PASSWORD_ERROR.getMsg());
        }
        else {
            UserVO userVO = new UserVO();
            userVO.setMobile(one.getMobile());
            userVO.setPassword(one.getPassword());
            userVO.setUserId(one.getUserId());
            userVO.setToken(JwtUtil.createToken(one.getUserId(),one.getMobile()));
            return ResultVOUtil.success(userVO);
        }
    }

    /**
     * token验证身份
     * @param request
     * @return
     */
    @GetMapping("/checkToken")
    public ResultVO checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        boolean checkToken = JwtUtil.checkToken(token);
        if (checkToken)
        {
            return ResultVOUtil.success("token验证成功");
        }
        return ResultVOUtil.fail("token验证失败");

    }

}

