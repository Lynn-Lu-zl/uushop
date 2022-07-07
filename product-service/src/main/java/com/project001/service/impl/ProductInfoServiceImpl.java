package com.project001.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project001.entity.ProductInfo;
import com.project001.exception.ShopException;
import com.project001.mapper.ProductCategoryMapper;
import com.project001.mapper.ProductInfoMapper;
import com.project001.result.ResponseEnum;
import com.project001.service.ProductInfoService;
import com.project001.vo.ProductExcelVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */

@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
   //点击alt+insert 实现方法implement method

    @Autowired
    //在ProductInfoMapper写自定义sql语句
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    @Override
    //接口2：怎么实现，调用ProductInfoMapper接口，实现ProductInfoMapper在xml定义的方法，然后回到ProductInfoServiceImpl返回结果，最后在BuyerProductController 控制台最终实现展示在页面
    public BigDecimal findPriceById(@PathVariable("id") Integer id) {
       //在ProductInfoMapper.xml写完后回到这里调用productInfoMapper的findPriceById方法，最后回到BuyerProductController使用自己封装的方法
        return this.productInfoMapper.findPriceById(id);
    }

    @Override
    public Boolean subStockById(Integer id, Integer quantity) {
        //接口4：调用ProductInfoMapper接口，实现ProductInfoMapper在xml定义的方法，
        // 先把库存查出（回到ProductInfoMapper写findStockById查找库存接口），购买后的库存是否<=0再判断是否能购买，库存不为0则更新库存（回到ProductInfoMapper写updateStockById更新库存接口），为0则抛出在common-service写好的ResponseEnum枚举异常提示"商品库存不足！")
        //最后回到BuyerProductController 控制台最终实现展示在页面
        Integer stock = this.productInfoMapper.findStockById(id);
        Integer result=stock-quantity;
        //运行时类异常会被UnifiedExceptionHandler错误处理器捕获，然后抛出错误信息
        if (result<0)throw new ShopException(ResponseEnum.PRODUCT_STOCK_ERROR.getMsg());
        Integer integer = this.productInfoMapper.updateStockById(id, result);
        return integer==1;//因为返回值是boolean，若integer==1则为true

    }

    /**
     * 导入导出表格
     *
     * @return
     */
    @Override
    public List<ProductExcelVO> productExcelVOList() {
        List<ProductInfo> productInfoList = this.productInfoMapper.selectList(null);
        List<ProductExcelVO> result = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            ProductExcelVO vo = new ProductExcelVO();
            BeanUtils.copyProperties(productInfo, vo);
            vo.setProductStatus("下架");
            if(productInfo.getProductStatus() == 1) vo.setProductStatus("上架");
            String nameByType = this.productCategoryMapper.getNameByType(productInfo.getCategoryType());
            vo.setCategoryName(nameByType);
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<ProductInfo> excleToProductInfoList(InputStream inputStream) {
        try {
            List<ProductInfo> list = new ArrayList<>();
            EasyExcel.read(inputStream)
                    .head(ProductExcelVO.class)
                    .sheet()
                    .registerReadListener(new AnalysisEventListener<ProductExcelVO>() {

                        @Override
                        public void invoke(ProductExcelVO excelData, AnalysisContext analysisContext) {
                            ProductInfo productInfo = new ProductInfo();
                            BeanUtils.copyProperties(excelData, productInfo);
                            if(excelData.getProductStatus().equals("正常")){
                                productInfo.setProductStatus(1);
                            }else{
                                productInfo.setProductStatus(0);
                            }
                            list.add(productInfo);
                        }

                        @Override
                        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        }
                    }).doRead();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
