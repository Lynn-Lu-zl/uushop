package com.project001.service.impl;

import com.project001.entity.Admin;
import com.project001.mapper.AdminMapper;
import com.project001.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-04
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
