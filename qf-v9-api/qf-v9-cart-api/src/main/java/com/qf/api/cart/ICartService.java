package com.qf.api.cart;

import com.qf.v9.common.pojo.ResultBean;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/25
 */
public interface ICartService {

    //添加，关键的参数，商品id，购买的数量--跟业务相关的
    //跟实现方案细节相关的 key 确定购物车
    public ResultBean add(String key,Long productId,Integer count);
    //更新数量
    public ResultBean updateCount(String key,Long productId,Integer count);
    //删除
    public ResultBean del(String key,Long productId);
    //查询
    public ResultBean list(String key);

    //合并购物车
    public ResultBean merge(String noLoginKey,String loginKey);


}
