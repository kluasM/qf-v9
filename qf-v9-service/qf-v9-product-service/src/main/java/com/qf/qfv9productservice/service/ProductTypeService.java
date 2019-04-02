package com.qf.qfv9productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.product.IProductTypeService;
import com.qf.v9.common.base.BaseServiceImpl;
import com.qf.v9.common.base.IBaseDao;
import com.qf.v9.entity.TProductType;
import com.qf.v9.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/8
 */
@Component
@Service
public class ProductTypeService extends BaseServiceImpl<TProductType> implements IProductTypeService{

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }

}
