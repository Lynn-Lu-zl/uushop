package com.project001.exception;

import com.project001.result.ResponseEnum;

import lombok.extern.slf4j.Slf4j;


//断言异常，逻辑业务判断封装，不用从业务层面判断，直接调用方法即可，如数据是否为空，两个值是否相等，不行就抛相应异常
//声明:如果不想每次都写private  final Logger logger = LoggerFactory.getLogger(当前类名.class); 可以用注解@Slf4j;
//log.info(),直接输出到了控制台
@Slf4j
public class Assert {

    public static void notNull(Object obj, ResponseEnum responseEnum){
        if(obj == null){
            log.info("数据为null");
            throw new ShopException(responseEnum.getMsg());
        }
    }

    public static void equals(Object obj1, Object obj2, ResponseEnum responseEnum) {
        if (!obj1.equals(obj2)) {
            log.info("{}和{}不相等",obj1,obj2);
            throw new ShopException(responseEnum.getMsg());
        }
    }

    public static void isTrue(boolean expression, ResponseEnum responseEnum) {
        if (!expression) {
            log.info("验证失败");
            throw new ShopException(responseEnum.getMsg());
        }
    }

}

