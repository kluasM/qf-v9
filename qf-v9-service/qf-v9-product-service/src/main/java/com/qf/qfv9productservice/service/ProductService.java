package com.qf.qfv9productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.api.product.IProductService;
import com.qf.api.vo.ProductVO;
import com.qf.v9.common.base.BaseServiceImpl;
import com.qf.v9.common.base.IBaseDao;
import com.qf.v9.entity.TProduct;
import com.qf.v9.entity.TProductDesc;
import com.qf.v9.mapper.TProductDescMapper;
import com.qf.v9.mapper.TProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/11
 */
@Component
@Service
public class ProductService extends BaseServiceImpl<TProduct> implements IProductService{

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<TProduct> list = productMapper.list();
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list,3);
        return pageInfo;
    }

    @Override
    public Long add(ProductVO productVO) {
        //1.添加商品的基本信息
        TProduct product = productVO.getProduct();
        //设置常规属性的值
        product.setFlag(true);
        product.setCreateTime(new Date());
        product.setUpdateTime(product.getCreateTime());
        product.setCreateUser(1L);
        product.setUpdateUser(product.getCreateUser());
        //设置主键回填
        productMapper.insertSelective(product);
        //2.添加商品的描述信息
        TProductDesc desc = new TProductDesc();
        desc.setProductId(product.getId());
        desc.setpDesc(productVO.getProductDesc());
        productDescMapper.insertSelective(desc);
        //返回商品的id
        return product.getId();
    }
}
