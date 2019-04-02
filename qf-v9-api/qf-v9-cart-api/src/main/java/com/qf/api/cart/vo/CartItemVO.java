package com.qf.api.cart.vo;

import com.qf.v9.entity.TProduct;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeSet;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/25
 * 用于页面展示
 */
public class CartItemVO implements Serializable{
    private TProduct product;
    private Integer count;
    private Date updateTime;

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CartItemVO{" +
                "product=" + product +
                ", count=" + count +
                ", updateTime=" + updateTime +
                '}';
    }


}
