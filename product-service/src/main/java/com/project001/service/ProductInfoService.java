package com.project001.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project001.entity.ProductInfo;
import com.project001.vo.ProductExcelVO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */

public interface ProductInfoService extends IService<ProductInfo> {

    /**
     * 根据 ID 查询商品价格
     * @param id
     * @return
     */
    //接口2：先在ProductInfoService自定义findPriceById接口优化sql语句，在ProductInfoServiceImpl实现方法
    //BigDecimal返回结果的类型，Integer id为传入参数的类型
    public BigDecimal findPriceById(Integer id);

    //接口3:先在ProductInfoService自定义subStockById接口,在ProductInfoServiceImpl实现方法
    public Boolean subStockById(Integer id,Integer quantity);


    /**
     * 导入导出表格
     * @return
     */
    public List<ProductExcelVO> productExcelVOList();
    public List<ProductInfo> excleToProductInfoList(InputStream inputStream);







}
