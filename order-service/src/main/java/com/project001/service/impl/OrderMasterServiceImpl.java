package com.project001.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project001.dto.BarDTO;
import com.project001.dto.BaseLineDTO;
import com.project001.entity.OrderDetail;
import com.project001.entity.OrderMaster;
import com.project001.entity.ProductInfo;
import com.project001.feign.ProductFeign;
import com.project001.form.OrderForm;
import com.project001.form.ProductForm;
import com.project001.mapper.OrderDetailMapper;
import com.project001.mapper.OrderMasterMapper;
import com.project001.service.OrderMasterService;
import com.project001.util.EChartsColorUtil;
import com.project001.vo.BarDataVO;
import com.project001.vo.BarStyleVO;
import com.project001.vo.StackedInnerVO;
import com.project001.vo.StackedVO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-10-28
 */

@Service
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderMasterService {
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    //接口1：创建订单实现，将表单传入的数据(获取OrderForm、ProductForm的实体类属性)传到OrderMaster订单表实体类中，调用orderMasterMapper进入插入操作，最后回到BuyerOrderController调用接口，因为结果集返回的是订单编号，不但要创建订单到数据表order_master还要进行存储订单详情order_detail才能获得订单编号？
    @Override
    public String create(OrderForm orderForm) {
        /**
         *  1、创建订单
         */

        OrderMaster orderMaster = new OrderMaster();
        //orderForm的属性按数据表order_master顺序装载,创建时间和修改时间在repository-service的FillHandler处理过了会自动生成，orderId订单编号是随机生成的要在OrderMaster实体类中添加注解@TableId，选择生成类型IdType.ASSIGN_UUID，mybatis随机生成id
        orderMaster.setBuyerName(orderForm.getName());
        orderMaster.setBuyerPhone(orderForm.getPhone());
        orderMaster.setBuyerAddress(orderForm.getAddress());
        //买家id
        orderMaster.setBuyerOpenid(orderForm.getId());
        //订单状态，默认0新下单,1完成，2取消
        orderMaster.setOrderStatus(0);
        //支付状态，默认0未支付，1已支付
        orderMaster.setPayStatus(0);
        //订单总价，单价*数量，要计算，不能单纯传值，可以先在里面填new BigDecimal(100)充当赋值测试
        //要获取商品和单价productPrice和数量需要和数据表ProductInfo关联起来，新建ProductFeign接口，启动Feign后回到OrderMasterServiceImpl计算订单总价，将原来的赋值测试去掉，获取orderForm的items实体第二层数据items.for，遍历ProductForm得到前台传入的全部productId，调用productFeign.findPriceById方法将前台传入的参数productId从数据表查询到对应id的商品单价传入赋给price
        //orderMaster.setOrderAmount(new BigDecimal(100));
        //所有订单总价
        List<ProductForm> items = orderForm.getItems();
        BigDecimal amount = new BigDecimal(0);
        for (ProductForm item : items) {
            Integer productId = item.getProductId();
            BigDecimal price = this.productFeign.findPriceById(productId);
            //商品订单总价：商品1单价*数量+商品2单价*数量+...，现在multiply只是一种商品的总价，要计算所有购买商品的总价amount，将所有multiply相加
            BigDecimal multiply = price.multiply(new BigDecimal(item.getProductQuantity()));
            amount =amount.add(multiply );

        }
        //将计算出来的所有订单总价传到orderMaster的OrderAmount
        orderMaster.setOrderAmount(amount);
        //插入orderMaster实体类到orderMasterMapper进行映射数据表的数据
        int insert=this.orderMasterMapper.insert(orderMaster);

        /**
         * 2、存储订单详情，和计算订单总价同样的做法，
         */
        //ProductInfo的属性按数据表order_detail顺序装载,创建时间和修改时间在repository-service的FillHandler处理过了会自动生成，orderId订单编号是随机生成的要在OrderDetail实体类中添加注解@TableId，选择生成类型IdType.ASSIGN_UUID，mybatis随机生成id
        items = orderForm.getItems();
        for (ProductForm item : items) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderMaster.getOrderId());
            ProductInfo productInfo = this.productFeign.findById(item.getProductId());
            orderDetail.setProductId(productInfo.getProductId());
            orderDetail.setProductName(productInfo.getProductName());
            orderDetail.setProductPrice(productInfo.getProductPrice());
            orderDetail.setProductQuantity(item.getProductQuantity());
            orderDetail.setProductIcon(productInfo.getProductIcon());
            this.orderDetailMapper.insert(orderDetail);
        }

        //todo 将创建订单的消息存入MQ
        this.rocketMQTemplate.convertAndSend("myTop","有新的订单" );
        return orderMaster.getOrderId();
        //return insert==1;//因为返回值是boolean，若insert==1则为true，成功插入数据到数据表中
    }

    @Override
    public BarDataVO createBarData() {
        BarDataVO barDataVO = new BarDataVO();
        List<BarDTO> bar = this.orderMasterMapper.bar();
        List<String> names = new ArrayList<>();
        List<BarStyleVO> values = new ArrayList<>();
        for (BarDTO barDTO : bar) {
            names.add(barDTO.getName());
            BarStyleVO barStyleVO = new BarStyleVO();
            barStyleVO.setValue(barDTO.getValue());
            barStyleVO.setItemStyle(EChartsColorUtil.createItemStyle(barDTO.getValue()));
            values.add(barStyleVO);
        }
        barDataVO.setNames(names);
        barDataVO.setValues(values);
        return barDataVO;
    }

    @Override
    public Map<String, List> createBaseLineData() {
        List<BaseLineDTO> baseLineDTOS = this.orderMasterMapper.baseLine();
        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (BaseLineDTO baseLineDTO : baseLineDTOS) {
            names.add(baseLineDTO.getDate());
            values.add(baseLineDTO.getValue());
        }
        Map<String,List> map = new HashMap<>();
        map.put("names", names);
        map.put("values", values);
        return map;
    }

    @Override
    public StackedVO createStackedData() {
        StackedVO stackedVO = new StackedVO();
        List<String> names = this.orderMasterMapper.findAllNames();
        List<String> dates = this.orderMasterMapper.findAllDates();
        List<StackedInnerVO> datas = new ArrayList<>();
        for (String name : names) {
            List<Integer> list = this.orderMasterMapper.findDatas(name);
            StackedInnerVO stackedInnerVO = new StackedInnerVO();
            stackedInnerVO.setName(name);
            stackedInnerVO.setData(list);
            datas.add(stackedInnerVO);
        }
        stackedVO.setNames(names);
        stackedVO.setDates(dates);
        stackedVO.setDatas(datas);
        return stackedVO;
    }
}
