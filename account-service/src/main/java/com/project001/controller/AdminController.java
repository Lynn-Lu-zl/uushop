package com.project001.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project001.entity.Admin;
import com.project001.exception.ShopException;
import com.project001.form.AdminForm;
import com.project001.result.ResponseEnum;
import com.project001.service.AdminService;
import com.project001.util.ResultVOUtil;
import com.project001.utils.JwtUtil;
import com.project001.vo.AdminVO;
import com.project001.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;


    /**
     * 管理员登录，登录成功同时设置token
     * @param adminForm
     * @param bindingResult
     * @return
     */
    @GetMapping("/login")
    public ResultVO login(@Valid  AdminForm adminForm, BindingResult bindingResult){

        if (bindingResult.hasErrors())
        {
            //抛出异常，用户信息不能为空
            throw new ShopException(ResponseEnum.USER_INFO_NULL.getMsg());
        }
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername,adminForm.getUsername());
        wrapper.eq(Admin::getPassword,adminForm.getPassword());
        Admin one = adminService.getOne(wrapper);
        if (one == null)
        {
            //抛出异常，登录信息失效
            throw new ShopException(ResponseEnum.LOGIN_ERROR.getMsg());
        }
        else
        {
            AdminVO adminVO = new AdminVO();
            BeanUtils.copyProperties(one,adminVO);
            //设置token
            adminVO.setToken(JwtUtil.createToken(one.getAdminId(),one.getUsername()));
            return ResultVOUtil.success(adminVO);
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

