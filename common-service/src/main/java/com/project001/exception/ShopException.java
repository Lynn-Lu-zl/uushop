package com.project001.exception;

public class ShopException extends RuntimeException {
    //运行时类异常，定义一个有参的构造函数返回信息
    public ShopException(String message) {
        super(message);
    }
}

