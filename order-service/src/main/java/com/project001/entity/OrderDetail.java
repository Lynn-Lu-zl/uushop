package com.project001.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单详情表
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */

@Data
  @EqualsAndHashCode(callSuper = false)
    public class OrderDetail implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单详情编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
      private String detailId;

    private String orderId;

    private Integer productId;

      /**
     * 商品名称
     */
      private String productName;

      /**
     * 商品单价
     */
      private BigDecimal productPrice;

      /**
     * 商品数量
     */
      private Integer productQuantity;

      /**
     * 商品小图
     */
      private String productIcon;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      /**
     * 修改时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;


}
