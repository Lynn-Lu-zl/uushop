package com.project001.vo;

import lombok.Data;

import java.util.List;

@Data
public class StackedInnerVO {
    private String name;
    private String type = "line";
    private String stack = "销量";
    private List<Integer> data;
}
