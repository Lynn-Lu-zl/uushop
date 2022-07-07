package com.project001.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//Mybatisplus实现MetaObjectHandler接口自动更新创建时间更新时间，
// 向数据库插入一条数据的时候，少不了一些向createTime、updateTime这些类字段，每次插入的数据都要设置这些值，很烦，通过实现MetaObjectHandler接口重写insertFill、updateFill方法可以不用重新设置这些值

@Component
public class FillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

}

