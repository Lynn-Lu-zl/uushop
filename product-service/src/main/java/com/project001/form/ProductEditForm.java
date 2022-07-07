package com.project001.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project001.entity.ProductCategory;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 编辑商品信息
 */
@Data
public class ProductEditForm {
    @JsonProperty("id")
    @NotNull(message = "商品ID不能为空")
    private Integer productId;
    @JsonProperty("name")
    @NotEmpty(message = "商品名称不能为空")
    private String productName;
    @JsonProperty("price")
    @NotNull(message = "商品价格不能为空")
    private BigDecimal productPrice;
    @JsonProperty("stock")
    @NotNull(message = "商品库存不能为空")
    private Integer productStock;
    @JsonProperty("description")
    @NotEmpty(message = "商品描述不能为空")
    private String productDescription;
    @JsonProperty("icon")
    @NotEmpty(message = "商品图片不能为空")
    private String productIcon;
    @NotNull(message = "商品状态不能为空")
    private Boolean status;
    @NotNull(message = "商品分类不能为空")
    private ProductCategory category;
}
