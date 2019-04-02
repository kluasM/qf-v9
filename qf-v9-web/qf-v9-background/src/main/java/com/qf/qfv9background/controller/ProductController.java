package com.qf.qfv9background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.api.item.ItemService;
import com.qf.api.product.IProductService;
import com.qf.api.search.ISearchService;
import com.qf.api.vo.ProductVO;
import com.qf.v9.common.constant.RabbitMQConstant;
import com.qf.v9.entity.TProduct;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/11
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference
    private IProductService productService;

    @Reference
    private ISearchService searchService;

    @Reference
    private ItemService itemService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("list")
    public String list(Model model){
        List<TProduct> list = productService.list();
        model.addAttribute("list",list);
        return "product/list";
    }

    @GetMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model,
                       @PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize){
        PageInfo<TProduct> pageInfo = productService.page(pageIndex,pageSize);
        model.addAttribute("pageInfo",pageInfo);
        return "product/list";
    }

    @PostMapping("add")
    public String add(ProductVO productVO){
        //返回添加后的商品ID
        Long productId = productService.add(productVO);

        //发送消息到交换机即可
        rabbitTemplate.convertAndSend(RabbitMQConstant.BACKGROUND_EXCHANGE,"product.add",productId);

        //商品id后续用
        //调用搜索服务，让其同步这个数据
        //searchService: id->mapper->product->document->solr
        //searchService: product->docuemnt->solr
        /*TProduct product = productVO.getProduct();
        System.out.println(product);//为什么没有id，跨JVM */
        //searchService.updateById(productId);

        //itemService:id->mapper->product->item.ftl->id.html
        //itemService.createHtmlById(productId);

        return "redirect:/product/page/1/1";
    }

}
