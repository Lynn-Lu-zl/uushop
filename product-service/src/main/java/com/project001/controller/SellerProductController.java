package com.project001.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project001.entity.ProductCategory;
import com.project001.entity.ProductInfo;
import com.project001.exception.ShopException;
import com.project001.form.ProductEditForm;
import com.project001.form.ProductForm;
import com.project001.handler.CustomCellWriteHandler;
import com.project001.mapper.ProductCategoryMapper;
import com.project001.mapper.ProductInfoMapper;
import com.project001.result.ResponseEnum;
import com.project001.service.ProductCategoryService;
import com.project001.service.ProductInfoService;
import com.project001.util.ResultVOUtil;
import com.project001.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 卖家端 前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */
@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductInfoMapper productInfoMapper;


    /**
     *  查询所有商品分类
     * @return
     * "content":[
     *       {
     *         "name": "热销榜",
     *         "type": 2,
     *         "goods": null
     *       },
     *       ]
     */
    @GetMapping("/findAllProductCategory")
    public ResultVO findAllProductCategory()
    {

        List<ProductCategory> list = productCategoryService.list();
        ArrayList<ProductCategoryVO> voArrayList = new ArrayList<>();
        for (ProductCategory productCategory : list) {
            ProductCategoryVO productCategoryVO = new ProductCategoryVO();
            productCategoryVO.setName(productCategory.getCategoryName());
            productCategoryVO.setType(productCategory.getCategoryType());
            //遍历的商品分类信息添加到动态数组中
            voArrayList.add(productCategoryVO);
        }
        //新建map包裹住所有的动态数组
        Map<String ,List>  map = new HashMap<>();
        map.put("content",voArrayList);
        return ResultVOUtil.success(map);
    }

    /**
     * 查询商品信息
     * 遍历商品信息ProductInfo将其赋值给SellerProductVO
     * ArrayList：content
     * map：content、size、total
     * @param page
     * @param size
     * @return
     * "content": [
     *       {
     *         "status": true,
     *         "id": 1,
     *         "name": "肉夹馍",
     *         "price": 16,
     *         "stock": 107,
     *         "description": "好吃好吃",
     *         "icon": "https://s1.st.meishij.net/rs/50/123/6030800/n6030800_152708155351112.jpg",
     *         "categoryName": "热销榜"
     *       }
     *     ],
     *     "size": 5,
     * 	"total": 11
     */
    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,
                         @PathVariable("size") Integer size)
    {
        Page<ProductInfo> productInfoPage = new Page<>(page,size);
        Page<ProductInfo> productInfoPage1 = productInfoService.page(productInfoPage);
        //获取所有商品信息
        List<ProductInfo> records = productInfoPage1.getRecords();
        List list = new ArrayList<SellerProductVO>();
        for (ProductInfo productInfo : records) {
            SellerProductVO productVO = new SellerProductVO();
            BeanUtils.copyProperties(productInfo,productVO);
            //判断商品是否为上架状态
            if (productInfo.getProductStatus() == 1)
            {
                productVO.setStatus(true);
            }
            //通过分类编号查询分类名称
            String productCategoryName = productCategoryService.getNameByType(productInfo.getCategoryType());
            list.add(productCategoryName);
            list.add(productVO);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("content", list);
        map.put("size", productInfoPage1.getSize());
        map.put("total",productInfoPage1.getTotal());
        return ResultVOUtil.success(map);
    }

    /**
     *  商品模糊查询
     * 根据关键字查询商品
     * @param page
     * @param size
     * @param keyWord
     * @return
     */
    @GetMapping("/like/{keyWord}/{page}/{size}")
    public ResultVO like(@PathVariable("page") Integer page,
                         @PathVariable("size") Integer size,
                         @PathVariable("keyWord") String keyWord)
    {
        Page<ProductInfo> productInfoPage = new Page<>(page,size);

        LambdaQueryWrapper<ProductInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(ProductInfo::getProductName,keyWord);

        Page<ProductInfo> productInfoPage1 = productInfoService.page(productInfoPage,wrapper);
        //获取所有商品信息
        List<ProductInfo> records = productInfoPage1.getRecords();
        List list = new ArrayList<SellerProductVO>();
        for (ProductInfo productInfo : records) {
            SellerProductVO productVO = new SellerProductVO();
            BeanUtils.copyProperties(productInfo,productVO);
            //判断商品是否为上架状态
            if (productInfo.getProductStatus() == 1)
            {
                productVO.setStatus(true);
            }
            //通过分类编号查询分类名称
            String productCategoryName = productCategoryService.getNameByType(productInfo.getCategoryType());
            productVO.setCategoryName(productCategoryName);
            list.add(productVO);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("content", list);
        map.put("size", productInfoPage1.getSize());
        map.put("total",productInfoPage1.getTotal());
        return ResultVOUtil.success(map);
    }

    /**
     *  通过分类查询商品
     * @param page
     * @param size
     * @param categoryType
     * @return
     */
    @GetMapping("/findByCategory/{categoryType}/{page}/{size}")
    public ResultVO findByCategory(@PathVariable("page") Integer page,
                                   @PathVariable("size") Integer size,
                                   @PathVariable("categoryType") Integer categoryType)
    {
        Page<ProductInfo> productInfoPage = new Page<>(page,size);

        LambdaQueryWrapper<ProductInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductInfo::getCategoryType,categoryType);

        Page<ProductInfo> productInfoPage1 = productInfoService.page(productInfoPage,wrapper);
        //获取所有商品信息
        List<ProductInfo> records = productInfoPage1.getRecords();
        List list = new ArrayList<SellerProductVO>();
        for (ProductInfo productInfo : records) {
            SellerProductVO productVO = new SellerProductVO();
            BeanUtils.copyProperties(productInfo,productVO);
            //判断商品是否为上架状态
            if (productInfo.getProductStatus() == 1)
            {
                productVO.setStatus(true);
            }
            //通过分类编号查询分类名称
            String productCategoryName = productCategoryService.getNameByType(productInfo.getCategoryType());
            productVO.setCategoryName(productCategoryName);
            list.add(productVO);

        }

        Map<String, Object> map = new HashMap<>();
        map.put("content", list);
        map.put("size", productInfoPage1.getSize());
        map.put("total",productInfoPage1.getTotal());
        return ResultVOUtil.success(map);
    }


    /**
     *  通过 ID 查询商品
     * @param id
     * @return
     * "data": {
     *     "status": true,
     *     "id": 1,
     *     "name": "肉夹馍",
     *     "price": 16,
     *     "stock": 107,
     *     "description": "好吃好吃",
     *     "icon": "https://s1.st.meishij.net/rs/50/123/6030800/n6030800_152708155351112.jpg",
     *     "category": {
     *         "categoryType": 2
     *     }
     *   }
     */
    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable Integer id)
    {
        ProductInfo productInfo = productInfoService.getById(id);
        SellerProductVOById productVOById = new SellerProductVOById();
        BeanUtils.copyProperties(productInfo,productVOById );
        //上架状态的商品
        if (productInfo.getProductStatus() == 1)
        {
            productVOById.setStatus(true);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("categoryType", productInfo.getCategoryType());
        productVOById.setCategory(map);
        return ResultVOUtil.success(productVOById);
    }

    /**
     * 添加商品
     * @param productForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/add")
    public ResultVO add(@Valid @RequestBody ProductForm productForm, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            throw new ShopException(ResponseEnum.PRODUCT_EMPTY.getMsg());
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productForm,productInfo);

        boolean save = productInfoService.save(productInfo);
        if (save)
        {
            return ResultVOUtil.success("添加成功");
        }
        return ResultVOUtil.fail("添加失败");
    }


    /**
     * 修改商品状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/updateStatus/{id}/{status}")

    public ResultVO updateStatus(@PathVariable("id") Integer id,
                                 @PathVariable("status") Boolean status)
    {
        Integer statusint = 0;
        if (status)
        {
            statusint = 1;
        }
        Boolean aBoolean = productInfoMapper.updateStatusById(id, statusint);
        if (aBoolean && status==true )
        {
            return ResultVOUtil.success(true);
        }
        if (aBoolean && status==false )
        {
            return ResultVOUtil.success(false);
        }
        return ResultVOUtil.fail(null);
    }

    /**
     * 修改商品信息
     * @param productEditForm
     * @param bindingResult
     * @return
     */
    @PutMapping("/update")
    public ResultVO update(@Valid @RequestBody ProductEditForm productEditForm, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            throw new ShopException(ResponseEnum.PRODUCT_EMPTY.getMsg());
        }

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productEditForm,productInfo);
        //默认设置为下架
        productInfo.setProductStatus(0);
        if (productEditForm.getStatus() == true)
        {
            productInfo.setProductStatus(1);
        }
        productInfo.setCategoryType(productEditForm.getCategory().getCategoryType());
        boolean b = productInfoService.updateById(productInfo);
        if (b)
        {
            return ResultVOUtil.success("修改商品成功");
        }

        return ResultVOUtil.success("修改商品失败");


    }

    /**
     *  通过 ID 删除商品
     * @param id
     * @return
     */
   @DeleteMapping("/delete/{id}")
    public ResultVO deleteById(@PathVariable Integer id){
        boolean b = productInfoService.removeById(id);
        if (b)
        {return ResultVOUtil.success("删除成功");}
        return ResultVOUtil.fail("删除失败");
    }


    /**
     * 导入Excel表格
     * @param file
     * @return
     */
    @PostMapping("/import")
    public ResultVO importData(@RequestParam("file") MultipartFile file){
        List<ProductInfo> productInfoList = null;
        try {
            productInfoList =productInfoService.excleToProductInfoList(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(productInfoList==null){
            return ResultVOUtil.fail("导入Excel失败！");
        }
        boolean result = this.productInfoService.saveBatch(productInfoList);
        if(result)return ResultVOUtil.success(null);
        return ResultVOUtil.fail("导入Excel失败！");
    }

    /**
     * 导出Excel表格
     * @param response
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("商品信息", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            List<ProductExcelVO> productExcelVOList = productInfoService.productExcelVOList();
            EasyExcel.write(response.getOutputStream(), ProductExcelVO.class)
                    .registerWriteHandler(new CustomCellWriteHandler())
                    .sheet("商品信息")
                    .doWrite(productExcelVOList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

