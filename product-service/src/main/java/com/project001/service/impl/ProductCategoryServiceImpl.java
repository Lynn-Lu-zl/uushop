package com.project001.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project001.entity.ProductCategory;
import com.project001.entity.ProductInfo;
import com.project001.mapper.ProductCategoryMapper;
import com.project001.mapper.ProductInfoMapper;
import com.project001.service.ProductCategoryService;
import com.project001.vo.ProductCategoryVO;
import com.project001.vo.ProductInfoBuyerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    //@Autowired依赖注入变量，然后变量会爆红，在ProductCategoryMapper在注解@Repository也可忽略
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    //ProductInfoMapper注解@Repository
    private ProductInfoMapper productInfoMapper;

    //查询列表构造实现方法，然后在ProductCategoryService接口直接用方法
    @Override
    public List<ProductCategoryVO> findAllProductCategoryVO() {
        //1、先查询所有的分类code、msg、data（暂时为空），再是分类下第二层data的信息name，type，goods（暂时为空），最后是第三层goods的信息id、price等
        //（1）获取所有分类，新建ProductCategory类目集合productCategoryList
        List<ProductCategory> productCategoryList = this.productCategoryMapper.selectList(null);


        //（2）展示到页面的数据格式要变成文档上的格式，name有哪些，type是什么，定义ProductCategoryVO第二层信息集合productCategoryVOList
        List<ProductCategoryVO> productCategoryVOList = new ArrayList<>();


        //(3)遍历分类productCategoryList二层信息集合
        for (ProductCategory productCategory : productCategoryList) {
            //创建ProductCategoryVO对象装载定义好的name，type
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            //赋值，完成实体类到视图转换，获取ProductCategory实体类中CategoryName、CategoryType赋给ProductCategoryVO的name、type
            productCategoryVO.setName(productCategory.getCategoryName());
            productCategoryVO.setType(productCategory.getCategoryType());

            //给分类添加第三层的商品信息
//定义第三层信息,使用mp的QueryWrapper，根据分类条件，查询商品记录,实体对象封装操作类

            QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
            //根据表productcategory和表productinfo的外键category_Type查询同一分类的所有商品信息
            queryWrapper.eq("category_Type", productCategory.getCategoryType());
            //获取查询到的第三层信息，新建ProductInfo商品表集合productInfoList
            List<ProductInfo> productInfoList = this.productInfoMapper.selectList(queryWrapper);
            //展示到页面的数据格式要变成文档上的格式，id，price有哪些，定义ProductInfoBuyerVO第三层信息集合productInfoBuyerVOList
            List<ProductInfoBuyerVO>productInfoBuyerVOList = new ArrayList<>();

            //(4)嵌套遍历第三层productInfoList
            for (ProductInfo productInfo : productInfoList) {
                ProductInfoBuyerVO productInfoBuyerVO = new ProductInfoBuyerVO();
                //对象属性之间赋值，但是对象的赋值属性比较多的时候，会想到用BeanUtils.copyProperties进行拷贝，将前者的属性copy给后者，属性名要一样，因为属性名都是以productxx格式和API接口文档不符，用@JsonProperty 此注解用于属性上，作用是把该属性的名称改为id、price
                //BeanUtils提供对Java反射和自省API的包装。其主要目的是利用反射机制对JavaBean的属性进行处理，BeanUtils.copyProperties通过java反射将类中当前属性字段对应的内容复制到另外一个类中，属性必须具有get/set方法，不然拷贝值为null
                BeanUtils.copyProperties(productInfo, productInfoBuyerVO);
                //将装载完第三层信息的productInfoBuyerVO添加到集合productInfoBuyerVOList
                productInfoBuyerVOList.add(productInfoBuyerVO);

            }

            //将装载完第二层信息的productCategoryVO添加到集合productCategoryList
            productCategoryVOList.add(productCategoryVO);
            //将productInfoBuyerVOList嵌套到商品列表的goods数据中
            productCategoryVO.setGoods(productInfoBuyerVOList);



        }

        //返回页面的数据
        return productCategoryVOList;
    }

    /**
     * 通过分类编号查询分类名称
     * @param categoryType
     * @return
     */
    @Override
    public String getNameByType(Integer categoryType) {
        return productCategoryMapper.getNameByType(categoryType);
    }
}
