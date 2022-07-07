package com.project001.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project001.entity.ProductInfo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */
@Repository
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    //将java对象变成mybatis中的mapper实例对象集映射，也是通过接口+实现类，只不过这里的实现类是xml,点击选择mybatis generator statement跳转到ProductInfoMapper.xml

    //承包在ProductInfoServiceImpl写的接口2实现方法的映射
    public BigDecimal findPriceById(Integer id);

    //接口4，先定义findStockById接口自定义sql语句查出库存，把库存查出来看是否够，若库存不为0则定义updateStockById接口在购买后更新库存
    public Integer  findStockById(Integer id);
    public Integer updateStockById(Integer id,Integer result);

    /**
     *根据id 修改商品状态
     * @param id
     * @param status
     * @return
     */
    public Boolean updateStatusById(Integer id,Integer status);





}
