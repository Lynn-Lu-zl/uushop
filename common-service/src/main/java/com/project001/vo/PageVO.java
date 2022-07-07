package com.project001.vo;

import lombok.Data;

import java.util.List;

//统一封装成对象，分页，项目文档查询商品、订单等有分页要求，如何满足不同类型的内容查询需求，内容使用泛型
@Data
public class PageVO<T> {
    //数据内容
    private List<T> content;
    //一页多少条数据
    private Long size;
    //总数据
    private Long total;
}
