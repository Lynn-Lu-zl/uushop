package com.project001.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

//要展示到视图的实体类
@Data
public class ProductInfoBuyerVO {

    @JsonProperty("id")
    private Integer productId;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("price")
    private BigDecimal productPrice;
    @JsonProperty("description")
    private String productDescription;
    @JsonProperty("icon")
    private String productIcon;

    private Integer quantity = 0;

    @JsonProperty("stock")
    private Integer productStock;






}
