package com.project001.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project001.entity.OrderMaster;
import com.project001.mapper.OrderMasterMapper;
import com.project001.service.OrderMasterService;
import com.project001.util.ResultVOUtil;
import com.project001.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单详情表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */
@RestController
@RequestMapping("/seller/order")
public class SellerProductController {

    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderMasterMapper orderMasterMapper;

    /**
     *查询订单
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,
                         @PathVariable("size") Integer size)
    {

        Page<OrderMaster> orderMasterPage = new Page<>(page, size);
        Page<OrderMaster> page1 = orderMasterService.page(orderMasterPage);
        Map<String, Object> map = new HashMap<>();
        map.put("content", page1.getRecords());
        map.put("size", page1.getSize());
        map.put("total",page1.getTotal());
        return ResultVOUtil.success(map);

    }

    /**
     *卖家端取消订单
     * 将订单状态改为2
     * @param orderId
     * @return
     */
    @PutMapping("/cancel/{orderId}")
    public ResultVO cancel(@PathVariable("orderId") String orderId)
    {
        Boolean cancel=orderMasterMapper.updateStatusByOrderId(orderId,2);
        if (cancel)
        {
            return ResultVOUtil.success(null);
        }
        return ResultVOUtil.fail(null);
    }

    /**
     * 卖家端完成订单
     * 将订单状态改为1
     * @param orderId
     * @return
     */
    @PutMapping("/finish/{orderId}")
    public ResultVO finish(@PathVariable("orderId") String orderId)
    {
        Boolean cancel=orderMasterMapper.updateStatusByOrderId(orderId,1);
        if (cancel)
        {
            return ResultVOUtil.success(null);
        }
        return ResultVOUtil.fail(null);
    }

    /**
     * 柱状图
     * @return
     */
    @GetMapping("/barSale")
    public ResultVO barSale(){
        return ResultVOUtil.success(this.orderMasterService.createBarData());
    }

    /**
     * 基础折线图
     * @return
     */
    @GetMapping("/basicLineSale")
    public ResultVO basicLineSale(){
        return ResultVOUtil.success(this.orderMasterService.createBaseLineData());
    }

    /**
     * 折线图折叠
     * @return
     */
    @GetMapping("/stackedLineSale")
    public ResultVO stackedLineSale(){
        return ResultVOUtil.success(this.orderMasterService.createStackedData());
    }

}

