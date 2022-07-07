package com.project001.form;

import lombok.Data;

@Data
/**
 * 别忘了加上注解才能有get，set，其他类才会访问得到
 */
public class ProductForm {
    //第二层ProductForm表单的数据实体类productId、productQuantity，用于计算数据表中的order_amount，订单总金额=单价*购买数量
    private Integer  productId;
    private Integer productQuantity;
}
