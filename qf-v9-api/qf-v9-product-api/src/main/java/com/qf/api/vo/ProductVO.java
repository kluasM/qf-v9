package com.qf.api.vo;

import com.qf.v9.entity.TProduct;

import java.io.Serializable;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/11
 */
public class ProductVO implements Serializable{
    private TProduct product;
    private String productDesc;

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
