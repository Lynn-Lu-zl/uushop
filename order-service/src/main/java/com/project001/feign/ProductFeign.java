package com.project001.feign;

import com.project001.entity.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@Repository
@FeignClient("product-com.project001.service")
public interface ProductFeign {

    //findPriceById接口实现方法的路径是调用product-service然后获取商品单价id，然后到OrderMasterServiceImpl依赖注入ProductFeign，再到启动类添加注解@EnableFeignClients，现在可以获取订单总价了回到OrderMasterServiceImpl
    @GetMapping("/buyer/product/findPriceById/{id}")
    public BigDecimal findPriceById(@PathVariable("id")Integer id);

    //findById接口实现方法的路径是调用product-service然后获取商品product_id
    @GetMapping("/buyer/product/findById/{id}")
    public ProductInfo findById(@PathVariable("id") Integer id);



}
