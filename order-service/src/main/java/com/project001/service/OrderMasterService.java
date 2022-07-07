package com.project001.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project001.entity.OrderMaster;
import com.project001.form.OrderForm;
import com.project001.vo.BarDataVO;
import com.project001.vo.StackedVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */
public interface OrderMasterService extends IService<OrderMaster> {
    //接口1：先在OrderMasterService自定义create接口，OrderMasterServiceImpl实现方法
    public String create(OrderForm orderForm);


    public BarDataVO createBarData();
    public Map<String, List> createBaseLineData();
    public StackedVO createStackedData();

}
