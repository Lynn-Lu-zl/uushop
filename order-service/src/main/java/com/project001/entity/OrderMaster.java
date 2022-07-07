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
 * 订单表
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class OrderMaster implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单编号，添加注解@TableId选择生成类型IdType.ASSIGN_UUID，mybatis随机生成id，点击源码可看到类型
     */
    @TableId(type = IdType.ASSIGN_UUID)
      private String orderId;

      /**
     * 买家名字
     */
      private String buyerName;

      /**
     * 买家电话
     */
      private String buyerPhone;

      /**
     * 买家地址
     */
      private String buyerAddress;

      /**
     * 买家id
     */
      private Integer buyerOpenid;

      /**
     * 订单总金额，单价*数量
     */
      private BigDecimal orderAmount;

      /**
     * 订单状态，默认0新下单,1完成，2取消
     */
      private Integer orderStatus;

      /**
     * 支付状态，默认0未支付，1已支付
     */
      private Integer payStatus;

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
