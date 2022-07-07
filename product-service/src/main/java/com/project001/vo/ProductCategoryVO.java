package com.project001.vo;

import lombok.Data;

import java.util.List;

//vo：后台要展示到视图的实体类，form：前台传到后台的实体类
@Data
//商品列表的第二层信息
public class ProductCategoryVO {
    private String name;
    private Integer type;
    //goods包含第三层信息，所以数据类型用ProductInfoBuyerVO
    private List<ProductInfoBuyerVO> goods;
}
