package com.project001.exception;

import com.project001.util.ResultVOUtil;
import com.project001.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
//设置了两种异常，Assert和ShopException，一旦抛出异常这里就会捕获到然后进行处理
//错误处理器，一切错误都会被这里捕获并返回对应的错误信息
public class UnifiedExceptionHandler {

    //controller中就不需要再去写大量的try-catch了，RestControllerAdvice会自动帮助catch,并匹配相应的ExceptionHandler,然后重新封装异常信息，返回值，统一格式返回给前端
    //@ExceptionHandler注解声明异常处理方法
    @ExceptionHandler(value = Exception.class)
    //handlerException会处理所有controller层抛出的Exception及其子层异常
    public ResultVO handlerException(Exception e){
        log.info("服务器内部异常，{}", e.getMessage());
        return ResultVOUtil.fail(e.getMessage());
    }

}