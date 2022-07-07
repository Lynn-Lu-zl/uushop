package com.project001.vo;

import lombok.Data;

import java.util.List;

/**
 * 商品分类
 */
@Data
public class CategoryVO {
    private String name;
    private Integer type;
    private List goods;
}
