package com.project001.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project001.entity.OrderDetail;
import com.project001.entity.OrderMaster;
import com.project001.exception.ShopException;
import com.project001.form.OrderForm;
import com.project001.mapper.OrderMasterMapper;
import com.project001.result.ResponseEnum;
import com.project001.service.OrderDetailService;
import com.project001.service.OrderMasterService;
import com.project001.util.ResultVOUtil;
import com.project001.vo.OrderDetailVO;
import com.project001.vo.OrderMasterVO;
import com.project001.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */
@RestController
@RequestMapping("/buyer/order")
public class BuyerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterMapper orderMasterMapper;




    /**
     *买家创建订单
     * @param orderForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public ResultVO create(@Valid @RequestBody OrderForm orderForm, BindingResult bindingResult){
        //如果bingingResult这个结果有错误（hasErrors）则抛出异常，显示错误信息
        if (bindingResult.hasErrors()){
            throw new ShopException(ResponseEnum.ORDER_CREATE_ERROR.getMsg());
        }
       // Boolean aBoolean = this.orderMasterService.create(orderForm);
        //将结果集用map封装
        String orderId = this.orderMasterService.create(orderForm);
        if (orderId!=null){
            Map<String,String> map=new HashMap<>();
            map.put("orderId", orderId);
            //响应成功返回orderId
            return ResultVOUtil.success(map);
        }
        return ResultVOUtil.fail(null);

    }


    /**
     * 根据买家id查询订单列表
     *
     * @param buyerId 买家id
     * @param page 分页
     * @param size 页码
     * @return 分页查询数据getRecords
     */
    @GetMapping("/list/{buyerId}/{page}/{size}")
    public ResultVO list(@PathVariable("buyerId") Integer buyerId,
                         @PathVariable("page") Integer page,
                         @PathVariable("size") Integer size)
    {
        //条件查询用QueryWrapper，定义OrderMaster实体对象封装操作类的QueryWrapper对象queryWrapper，按照传入的参数{buyerId}映射到数据表buyer_openid字段获取商品信息
        // 把传入参数{page}/{size}装到page1里，调用orderMasterService原生接口page实现分页查询，将所有查询记录展示
        //Page<>(page, size)，page设置查询的第几页，size记录总数，如id=1的订单一共有12条记录，输入id=1，page=1，size=5表示将12条分成12/5=3页，查询第一页的1-5条记录，page改成2则是6-10，条page=3则是11-12条

        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_openid",buyerId);
        Page<OrderMaster> page1 = new Page<>(page, size);
        //page(page1, queryWrapper)，page1分页查询条件，queryWrapper实体对象封装操作类
        Page page2 = this.orderMasterService.page(page1, queryWrapper);
        return  ResultVOUtil.success(page2.getRecords());
    }



    /**
     * 根据买家id、订单id查询该订单详情
     * @param buyerId
     * @param orderId
     * @return  返回数据data格式和现有实体类不匹配（缺少orderDetailList），新建VO类
     */
    @GetMapping("/detail/{buyerId}/{orderId}")
    public ResultVO detail(@PathVariable("buyerId") Integer buyerId,
                           @PathVariable("orderId") String orderId)
    {
        /**
         * 获取OrderMasterVO，剩下OrderDetailVO
         */
        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_openid", buyerId);
        queryWrapper.eq("order_id", orderId);
        //根据单个buyerId查询订单，用getOne(queryWrapper)
        OrderMaster one = this.orderMasterService.getOne(queryWrapper);
        //将查到的数据one全部装到orderMasterVO，一般要一个个属性赋值，使用org.springframework.beans.BeanUtils.copyProperties方法进行对象之间属性的赋值，避免通过get、set方法一个一个属性的赋值，还剩下OrderDetailVO没有赋值，添加依赖注解OrderDetailService遍历OrderDetailVO
        OrderMasterVO orderMasterVO = new OrderMasterVO();
        BeanUtils.copyProperties(one, orderMasterVO);

        /**
         * 获取OrderDetailVO
         */
        QueryWrapper<OrderDetail> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_id", orderId );
        List<OrderDetail> list = this.orderDetailService.list(queryWrapper1);
        List<OrderDetailVO> orderDetailVOList=new ArrayList<>();
        //遍历list赋给orderDetail
        for (OrderDetail orderDetail : list) {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            //将查到的数据orderDetail全部装到orderDetailVO
            BeanUtils.copyProperties(orderDetail, orderDetailVO);
            //orderDetailVO放进orderDetailVOList集合容器
            orderDetailVOList.add(orderDetailVO);
        }
        //获取OrderMasterVO定义的OrderDetailVOList属性
        orderMasterVO.setOrderDetailVOList(orderDetailVOList);
        //返回OrderMasterVO+OrderDetailVOList拼接的数据
        return ResultVOUtil.success(orderMasterVO);

    }




    /**
     *根据买家id、订单id 取消该订单
     * orderStatus订单状态默认0新下单,1完成，2取消，修改orderStatus订单状态为2
     * @param buyerId
     * @param orderId
     * @return
     */
    @PutMapping("/cancel/{buyerId}/{orderId}")
    public ResultVO cancel( @PathVariable("buyerId") Integer buyerId,
                            @PathVariable("orderId") String orderId)
    {
        Boolean cancel = this.orderMasterMapper.cancel(buyerId, orderId);
        //API接口返回的data无论成功还是失败都为null
        if (cancel)
        {
            return ResultVOUtil.success(null);
        }
        return ResultVOUtil.fail(null);
    }



    /**
     * 完结订单，根据订单id，修改订单状态
     * orderStatus订单状态默认0新下单,1完成，2取消，修改orderStatus订单状态为1
     * @param orderId
     * @return
     */
    @PutMapping("/finish/{orderId}")
    public ResultVO finish(@PathVariable("orderId") String orderId)
    {
        Boolean finish = this.orderMasterMapper.finish(orderId);
        //API接口返回的data无论成功还是失败都为null
        if (finish){
            return ResultVOUtil.success(null);
        }
        return ResultVOUtil.fail(null);
    }



    /**
     * 根据买家id、订单id修改支付订单状态
     * payStatus支付状态，默认0未支付，1已支付，修改payStatus订单状态为1
     * @param buyerId
     * @param orderId
     * @return
     */
    @PutMapping("/pay/{buyerId}/{orderId}")
    public ResultVO pay(@PathVariable("buyerId") Integer buyerId,
                        @PathVariable("orderId") String orderId)
    {
        Boolean pay = this.orderMasterMapper.pay(buyerId, orderId);
        //API接口返回的data无论成功还是失败都为null
        if (pay)return ResultVOUtil.success("支付成功");
        return ResultVOUtil.fail("支付失败");
    }


}

