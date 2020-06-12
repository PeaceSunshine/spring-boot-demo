package com;

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

@SpringBootTest
//找不到RunWith注解是因为test maven依赖项中配置了exclusions
@RunWith(SpringRunner.class)
public class SearchServiceApplicationTests {

    @Autowired
    private SolrClient solrClient;

    @Test
   public void contextLoads() {
    }

    @Test
    public void addorUpdateTest() throws IOException, SolrServerException {
        //1.创建一个document对象
        SolrInputDocument document = new SolrInputDocument();
        //2.设置属性
        document.setField("id",999);
        document.setField("product_name","小米MIX666代");
        document.setField("product_price",19999);
        document.setField("product_sale_point","全球最高像素的手机");
        document.setField("product_images","123");
        //可以指定collection,默认添加到collection1中
        solrClient.add(document);
        //3.提交添加操作
        solrClient.commit();
    }

    @Test
    public void querySolrTest() throws IOException, SolrServerException {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        //product_name schema.xml中定义field name属性 与数据表无关
        //小米MIX4代 会分词
        queryCondition.setQuery("product_name:小米MIX4代");
//        queryCondition.setQuery("product_keywords:MIX4代");
        //2.执行查询
        QueryResponse response = solrClient.query(queryCondition);
        //3.得到查询结果
        SolrDocumentList solrDocuments = response.getResults();

        //4.遍历查询结果
        for (SolrDocument document : solrDocuments) {
            System.out.println(document.getFieldValue("product_name")+"->"+document.getFieldValue("product_price"));
        }
    }

    @Test
    public void delTest() throws IOException, SolrServerException {
        //solrClient.deleteByQuery("product_name:小米MIX4代");
        solrClient.deleteById("1");
        solrClient.commit();
    }
}
