package com.project001.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 卖家端商品信息
 */
@Data
public class SellerProductVO {
    @JsonProperty("id")
    private Integer productId;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("price")
    private BigDecimal productPrice;
    @JsonProperty("stock")
    private Integer productStock;
    @JsonProperty("description")
    private String productDescription;
    @JsonProperty("icon")
    private String productIcon;
    private String categoryName;
    private Boolean status;
}
