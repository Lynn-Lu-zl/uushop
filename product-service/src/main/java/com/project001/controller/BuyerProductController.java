package com.project001.controller;


import com.project001.entity.ProductInfo;
import com.project001.service.ProductCategoryService;
import com.project001.service.ProductInfoService;
import com.project001.util.ResultVOUtil;
import com.project001.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 * 买家端 前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoService productInfoService;



    /**
     * 查询商品列表
     * @return
     */
    @GetMapping("/list")
    public ResultVO list(){

        //返回结果集common-service写的ResultVOUtil
        return ResultVOUtil.success(this.productCategoryService.findAllProductCategoryVO());
    }



    /**
     * 根据 ID 查询商品价格
     * @param id
     * @return
     */
        @GetMapping("/findPriceById/{id}")
    public BigDecimal findPriceById(@PathVariable("id") Integer id){
        return this.productInfoService.findPriceById(id);
    }


    /**
     *通 过 ID 查询商品
     * @param id
     * @return
     */
    @GetMapping("/findById/{id}")
    public ProductInfo findById(@PathVariable("id") Integer id){
        return this.productInfoService.getById(id);
    }




    /**
     * 减库存
     * @param id
     * @param quantity
     * @return
     */
    @PutMapping("/subStockById/{id}/{quantity}")
    public Boolean subStockById(
            @PathVariable("id") Integer id,
            @PathVariable("quantity") Integer quantity
    ){
        return this.productInfoService.subStockById(id, quantity );

    }

}

