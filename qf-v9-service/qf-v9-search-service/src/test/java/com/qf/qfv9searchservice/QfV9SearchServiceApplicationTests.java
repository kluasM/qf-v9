package com.qf.qfv9searchservice;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QfV9SearchServiceApplicationTests {

	@Autowired
	private SolrClient solrClient;


	@Test
	public void addOrUpdateTest() throws IOException, SolrServerException {
		//作用于数据的同步操作上，源头数据库发生了变更，索引库也要跟着变更
		//solr里面操作的记录，document
		SolrInputDocument document = new SolrInputDocument();
		//需要有一个唯一的标识id
		document.setField("id","10002");
		document.setField("product_name","金立手机！");
		document.setField("product_price","19999");
		document.setField("product_sale_point","马云说，买手机我只买金立");
		document.setField("product_images","1111111");
		//提交
		solrClient.add("collection2",document);
		//solrClient.add(document);
		//solrClient.commit();
		solrClient.commit("collection2");
	}

	@Test
	public void queryTest() throws IOException, SolrServerException {
		//组装查询条件
		SolrQuery queryCondtion = new SolrQuery();
		//queryCondtion.setQuery("*:*");
		queryCondtion.setQuery("product_keywords:手机");
		//张学友刘德华同台演出 --> 分词之后再匹配 张学友
		//
		QueryResponse response = solrClient.query(queryCondtion);
		//
		SolrDocumentList results = response.getResults();
		//
		for (SolrDocument document : results) {
			System.out.println(document.get("id"));
			System.out.println(document.get("product_name"));
			System.out.println(document.get("product_sale_point"));
		}
	}

	@Test
	public void delTest() throws IOException, SolrServerException {
		solrClient.deleteByQuery("product_name:张学友刘德华同台演出");
		solrClient.commit();
	}



}
