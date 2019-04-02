package com.qf.qfv9searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.api.search.ISearchService;
import com.qf.v9.common.pojo.PageResultBean;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TProduct;
import com.qf.v9.mapper.TProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/14
 */
@Component
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean initAllData() {
        //1.获取到数据库的数据
        List<TProduct> list = productMapper.list();
        //2.将数据转换成document，再将保存到solr中
        for (TProduct product : list) {
            //product->document
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_images",product.getImage());
            document.setField("product_sale_point",product.getSalePoint());
            document.setField("product_price",product.getPrice());
            //
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                //打印到控制台
                e.printStackTrace();
                //记录日志
                //记录到日志表
                //异常信息管理---索引库异常信息
                //发送一封邮件和发送短信给相关负责人
                return ResultBean.error("添加到索引库失败！");
            }
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return ResultBean.error("添加到索引库失败！");
        }
        return ResultBean.success("数据同步成功！");
    }

    @Override
    public ResultBean updateById(Long id) {
        //1.获取到数据库的数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //2.将数据转换成document，再将保存到solr中
        //product->document
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_images",product.getImage());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_price",product.getPrice());
        //
        try {
            solrClient.add(document);
        } catch (SolrServerException | IOException e) {
            //打印到控制台
            e.printStackTrace();
            //记录日志
            //记录到日志表
            //异常信息管理---索引库异常信息
            //发送一封邮件和发送短信给相关负责人
            return ResultBean.error("添加到索引库失败！");
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return ResultBean.error("添加到索引库失败！");
        }
        return ResultBean.success("数据同步成功！");
    }

    @Override
    public List<TProduct> searchByKeyWord(String keyWord) {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(!StringUtils.isAllEmpty(keyWord)){
            queryCondition.setQuery("product_keywords:"+keyWord);
        }else{
            queryCondition.setQuery("product_keywords:华为");
        }
        //2.增加一个高亮的效果
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        queryCondition.addHighlightField("product_sale_point");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        List<TProduct> results = null;
        try {
            //2.执行查询
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList list = response.getResults();

            //获取到高亮的信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //
            results = new ArrayList<>(list.size());
            //3.将查询结果List<Docuemnt>转换为List<TProduct>
            for (SolrDocument document : list) {
                //document->product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setImage(document.getFieldValue("product_images").toString());
                product.setPrice(Long.parseLong(document.get("product_price").toString()));
                //product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                //获取商品名称的高亮信息
                //1，获取到当前这条记录的高亮信息
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id").toString());
                //2，获取商品名称的字段的高亮信息
                List<String> productNameHighlight = map.get("product_name");
                //3.单独处理高亮的设置
                if(productNameHighlight!= null && productNameHighlight.size() > 0){
                    //如果本次是按照商品名称搜索到的记录
                    product.setName(productNameHighlight.get(0));
                }else{
                    product.setName(document.getFieldValue("product_name").toString());
                }
                //获取商品卖点的高亮信息
                List<String> productSalePointHighlight = map.get("product_sale_point");
                if(productSalePointHighlight != null && productSalePointHighlight.size() > 0){
                    //按照商品卖点搜索到的记录
                    product.setSalePoint(productSalePointHighlight.get(0));
                }else{
                    product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                }


                results.add(product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public PageResultBean<TProduct> searchByKeyWord(String keyWord, Integer pageIndex, Integer rows) {
        PageResultBean<TProduct> pageResultBean = new PageResultBean<>();
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        if(!StringUtils.isAllEmpty(keyWord)){
            queryCondition.setQuery("product_keywords:"+keyWord);
        }else{
            queryCondition.setQuery("product_keywords:华为");
        }
        //2.增加一个高亮的效果
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        queryCondition.addHighlightField("product_sale_point");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //3.增加分页
        queryCondition.setStart((pageIndex-1)*rows);
        queryCondition.setRows(rows);

        List<TProduct> results = null;

        long totalCount = 0L;
        try {
            //2.执行查询
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList list = response.getResults();
            totalCount = list.getNumFound();

            //获取到高亮的信息
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //
            results = new ArrayList<>(list.size());
            //3.将查询结果List<Docuemnt>转换为List<TProduct>
            for (SolrDocument document : list) {
                //document->product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setImage(document.getFieldValue("product_images").toString());
                product.setPrice(Long.parseLong(document.get("product_price").toString()));
                //product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                //获取商品名称的高亮信息
                //1，获取到当前这条记录的高亮信息
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id").toString());
                //2，获取商品名称的字段的高亮信息
                List<String> productNameHighlight = map.get("product_name");
                //3.单独处理高亮的设置
                if(productNameHighlight!= null && productNameHighlight.size() > 0){
                    //如果本次是按照商品名称搜索到的记录
                    product.setName(productNameHighlight.get(0));
                }else{
                    product.setName(document.getFieldValue("product_name").toString());
                }
                //获取商品卖点的高亮信息
                List<String> productSalePointHighlight = map.get("product_sale_point");
                if(productSalePointHighlight != null && productSalePointHighlight.size() > 0){
                    //按照商品卖点搜索到的记录
                    product.setSalePoint(productSalePointHighlight.get(0));
                }else{
                    product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                }
                results.add(product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pageResultBean.setPageNum(pageIndex);
        pageResultBean.setPageSize(rows);
        pageResultBean.setList(results);
        pageResultBean.setTotal(totalCount);
        pageResultBean.setPages((int) (totalCount%rows==0?(totalCount/rows):(totalCount/rows)+1));
        return pageResultBean;
    }
}
