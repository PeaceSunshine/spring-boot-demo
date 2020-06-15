package com.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.search.ISearchService;
import com.entity.TProduct;
import com.mapper.TProductMapper;
import common.pojo.PageResultBean;
import common.pojo.ResultBean;
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
 * @Author lx
 * @Date 2020/6/12
 **/
@Component
@Service
public class SearchServiceImpl  implements ISearchService {

    @Autowired
    private TProductMapper productMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean initAllData() {
        //1.获取数据库数据
        List<TProduct> list = productMapper.list();
        //2.遍历list进行solrClient add操作
        for (TProduct product : list) {
            //3.将product转换成SolrInputDocument对象,设置属性
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_sale_point",product.getSalePoint());
            document.setField("product_images",product.getImage());
            //4.保存
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                //日志记录
                //记录到日志表
                //异常信息管理----不同模块的异常信息
                return ResultBean.error("数据同步错误!!!");
            }
        }
        //5.solrClient提交
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return ResultBean.error("同步数据失败！");
        }

        return  ResultBean.success("同步数据成功！");
    }

    @Override
    public List<TProduct> searchByKeyWord(String keyWord) {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        if (StringUtils.isNotBlank(keyWord)){
            queryCondition.setQuery("product_keywords:"+keyWord);
        }else{
            queryCondition.setQuery("product_keywords:M");
        }

        //开启高亮属性
        queryCondition.setHighlight(true);
        //指明为哪些字段设置高亮属性
        queryCondition.addHighlightField("product_name");
        queryCondition.addHighlightField("product_sale_point");
        //设置前缀后缀
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //2.查询
        QueryResponse response = null;
        List<TProduct> productList =null;
        try {
            response = solrClient.query(queryCondition);
            /*
            * 输入关键字M查询结果为0
            * 可能因为分词没有分出M 使用关键字MI是有结果的
            *
            * */
            SolrDocumentList list = response.getResults();
            productList = new ArrayList<>(list.size());

            //获取高亮属性
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //3.查询结果封装成list
            for (SolrDocument document : list) {
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                //product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setImage(document.getFieldValue("product_images").toString());

                //设置product_name高亮信息
                //获取当前记录的高亮信息
                Map<String, List<String>> highlight = highlighting.get(document.getFieldValue("id").toString());
                //获取对应字段的高亮属性=======================名称属性
                List<String> productHighLight = highlight.get("product_name");
                //3.判断该字段是否有高亮信息
                if(productHighLight != null && productHighLight.size() > 0){
                    //高亮信息
                    product.setName(productHighLight.get(0));
                }else {
                    //普通的文本信息
                    product.setName(document.getFieldValue("product_name").toString());
                }

                //获取对应字段的高亮属性==================卖点属性
                List<String> salePointHighLight = highlight.get("product_sale_point");
                //3.判断该字段是否有高亮信息
                if(salePointHighLight != null && salePointHighLight.size() > 0){
                    //高亮信息
                    product.setSalePoint(salePointHighLight.get(0));
                }else {
                    //普通的文本信息
                    product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                }
                //添加到productList
                productList.add(product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productList;
    }

    //增加分页功能
    @Override
    public PageResultBean<TProduct> searchByKeyWord(String keyWord, Integer pageIndex, Integer pageSized) {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        PageResultBean<TProduct> productPageResultBean = new PageResultBean<>();
        if (StringUtils.isNotBlank(keyWord)){
            queryCondition.setQuery("product_keywords:"+keyWord);
        }else{
            queryCondition.setQuery("product_keywords:M");
        }

        //开启高亮属性
        queryCondition.setHighlight(true);
        //指明为哪些字段设置高亮属性
        queryCondition.addHighlightField("product_name");
        queryCondition.addHighlightField("product_sale_point");
        //设置前缀后缀
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");

        //增加分页
        queryCondition.setStart((pageIndex-1)*pageSized);
        queryCondition.setRows(pageSized);

        //2.查询
        QueryResponse response = null;
        List<TProduct> productList =null;
        Long totalCount = 0L;
        try {
            response = solrClient.query(queryCondition);
            /*
             * 输入关键字M查询结果为0
             * 可能因为分词没有分出M 使用关键字MI是有结果的
             *
             * */
            SolrDocumentList list = response.getResults();
            productList = new ArrayList<>(list.size());
            totalCount =  list.getNumFound();

            //获取高亮属性
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            //3.查询结果封装成list
            for (SolrDocument document : list) {
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                //product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setImage(document.getFieldValue("product_images").toString());

                //设置product_name高亮信息
                //获取当前记录的高亮信息
                Map<String, List<String>> highlight = highlighting.get(document.getFieldValue("id").toString());
                //获取对应字段的高亮属性=======================名称属性
                List<String> productHighLight = highlight.get("product_name");
                //3.判断该字段是否有高亮信息
                if(productHighLight != null && productHighLight.size() > 0){
                    //高亮信息
                    product.setName(productHighLight.get(0));
                }else {
                    //普通的文本信息
                    product.setName(document.getFieldValue("product_name").toString());
                }

                //获取对应字段的高亮属性==================卖点属性
                List<String> salePointHighLight = highlight.get("product_sale_point");
                //3.判断该字段是否有高亮信息
                if(salePointHighLight != null && salePointHighLight.size() > 0){
                    //高亮信息
                    product.setSalePoint(salePointHighLight.get(0));
                }else {
                    //普通的文本信息
                    product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                }
                //添加到productList
                productList.add(product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productPageResultBean.setPageNum(pageIndex);
        productPageResultBean.setPageSize(pageSized);
        productPageResultBean.setTotal(totalCount);
        productPageResultBean.setList(productList);
        productPageResultBean.setPages((int) (totalCount%pageSized==0?(totalCount/pageSized):(totalCount/pageSized)+1));
        return productPageResultBean;
    }

    /**
     * 增加商品时更新solr索引库
     * @param id
     * @return
     */
    @Override
    public ResultBean updateById(Long id) {
        //1.获取数据库数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        System.out.println("搜索实现类 id:"+product.getId());
        //3.将product转换成SolrInputDocument对象,设置属性
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_images",product.getImage());
        //4.保存
        try {
            solrClient.add(document);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //日志记录
            //记录到日志表
            //异常信息管理----不同模块的异常信息
            return ResultBean.error("数据同步错误!!!");
        }

        //5.solrClient提交
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return ResultBean.error("同步数据失败！");
        }

        return  ResultBean.success("同步数据成功！");
    }
}
