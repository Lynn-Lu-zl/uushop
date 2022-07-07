package com.project001.vo;

import lombok.Data;

//ResultVO对象封装，封装ResultVO实现统一返回结果
// 通过RestFul接口开发的接口，一般含有接口执行状态（成功/失败、失败描述、成功的数据返回对象）
//从前台返回的结果有三个数据， "code"、 “msg”、“data”，统一封装成对象，因为data返回什么类型都有可能用泛型， 返回结果对象结构定义如下
@Data
public class ResultVO<T> {
    //错误代码
    private Integer code;
    //消息
    private String msg;
    //对应返回数据
    private T data;


}

