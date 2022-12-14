package com.project001.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserRegisterForm {
    @NotEmpty(message = "电话不能为空")
    private String mobile;
    @NotEmpty(message = "校验码不能为空")
    private String code;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
