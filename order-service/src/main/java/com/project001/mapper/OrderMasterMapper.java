package com.project001.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project001.dto.BarDTO;
import com.project001.dto.BaseLineDTO;
import com.project001.entity.OrderMaster;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */
@Repository
public interface OrderMasterMapper extends BaseMapper<OrderMaster> {
    //接口4： 取消订单，将传入的参数buyerId，orderId对应的order_status修改为2
    public Boolean cancel(Integer buyerId,String orderId);
    //接口5： 完结订单
    public Boolean finish(String orderId);
    // 接口6：支付订单
    public Boolean pay(Integer buyerId,String orderId);


    /**
     * 卖家取消订单
     * @param orderId
     * @param i
     * @return
     */
    Boolean updateStatusByOrderId(String orderId, int i);

    public List<BarDTO> bar();
    public List<BaseLineDTO> baseLine();
    public List<String> findAllNames();
    public List<String> findAllDates();
    public List<Integer> findDatas(String name);
}
