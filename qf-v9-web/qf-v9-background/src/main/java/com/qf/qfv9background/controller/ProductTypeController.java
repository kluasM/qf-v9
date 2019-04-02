package com.qf.qfv9background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.api.product.IProductTypeService;
import com.qf.v9.entity.TProductType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/8
 */
@RestController
@RequestMapping("productType")
public class ProductTypeController {

    @Reference
    private IProductTypeService productTypeService;

    @GetMapping("list")
    public List<TProductType> list(){
        return productTypeService.list();
    }

    @GetMapping("listForJsonp")
    public String listForJsonp(String callback){
        //获取要回调的客户端函数
        System.out.println("callback="+callback);
        //回调这个函数
        List<TProductType> list = productTypeService.list();
        //list --> json --->填充到回调函数
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return callback+"("+json+")";
        //jsonp=json+padding(填充)
    }
}
