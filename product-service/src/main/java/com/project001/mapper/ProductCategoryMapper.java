package com.project001.mapper;

import com.project001.entity.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 类目表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */
@Repository
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    public String getNameByType(Integer type);



}
