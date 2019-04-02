package com.qf.api.search;

import com.qf.v9.common.pojo.PageResultBean;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TProduct;

import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/14
 */
public interface ISearchService {
    ResultBean initAllData();

    ResultBean updateById(Long id);

    List<TProduct> searchByKeyWord(String keyWord);

    PageResultBean<TProduct> searchByKeyWord(String keyWord,Integer pageIndex,Integer rows);
}
