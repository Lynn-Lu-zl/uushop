package com.project001.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.project001.entity.ProductCategory;
import com.project001.vo.ProductCategoryVO;

import java.util.List;

/**
 * <p>
 * 类目表 服务类
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    //接口直接用实现方法，查询商品列表
    public List <ProductCategoryVO> findAllProductCategoryVO();


    /**
     * 通过分类编号查询分类名称
     * @param categoryType
     * @return
     */
    String getNameByType(Integer categoryType);
}
