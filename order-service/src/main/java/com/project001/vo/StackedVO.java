package com.project001.vo;

import lombok.Data;

import java.util.List;

@Data
public class StackedVO {
    private List<String> names;
    private List<String> dates;
    private List<StackedInnerVO> datas;
}
