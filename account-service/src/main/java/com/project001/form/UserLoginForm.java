package com.project001.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginForm {
    @NotEmpty(message = "电话不能为空")
    private String mobile;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
