package com.qf.api.product;

import com.github.pagehelper.PageInfo;
import com.qf.api.vo.ProductVO;
import com.qf.v9.common.base.IBaseService;
import com.qf.v9.entity.TProduct;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/11
 */
public interface IProductService extends IBaseService<TProduct>{
    PageInfo<TProduct> page(Integer pageIndex, Integer pageSize);

    Long add(ProductVO productVO);
}
