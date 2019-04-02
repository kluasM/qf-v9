package com.qf.qfv9search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.search.ISearchService;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TProduct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/14
 */
@Controller
@RequestMapping("search")
public class SearchController {

    //@Reference
    private ISearchService searchService;

    @RequestMapping("initAllData")
    @ResponseBody
    public ResultBean initAllData(){
        return searchService.initAllData();
    }

    @RequestMapping("searchByKeyWord")
    public String searchByKeyWord(String keyWord, Model model){
        //
        //List<TProduct> list = searchService.searchByKeyWord(keyWord);
        List<TProduct> list = new ArrayList<>();
        //
        model.addAttribute("list",list);
        return "search";
    }



}
