package com.project001.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderMasterVO {
    /**
     * 买家编号
     */
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

    private LocalDateTime createTime;

    /**
     * 修改时间
     */

    private LocalDateTime updateTime;

    /**
     * orderDetailList，API接口第二层，和OrderDetail差不多但是不需要时间，不能直接用OrderDetail实体类，新建OrderDetailVO重新定义
     */

    private List<OrderDetailVO> orderDetailVOList;
}
