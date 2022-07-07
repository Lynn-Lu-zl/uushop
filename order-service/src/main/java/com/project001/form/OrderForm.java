package com.project001.form;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
//接收前台的表单，封装成实体类
// vo：后台要展示到视图的实体类，form：前台传到后台的实体类，
public class OrderForm {
    @NotEmpty(message = "买家姓名不能为空")
    private String name;
    @NotEmpty(message = "买家电话不能为空")
    private String phone;
    @NotEmpty(message = "收货地址不能为空")
    private String address;
    @NotNull(message = "买家ID不能为空")
    private Integer id;
    @NotEmpty(message = "商品信息不能空")
    //泛型装items第二层ProductForm表单的数据实体类productId、productQuantity
    private List<ProductForm> items;
}
