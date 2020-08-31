package com.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.search.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author lx
 * @Date 2020/6/23
 * @Description
 **/
@Component
@RabbitListener(queues = "center-product-exchange-search-queue")
@Slf4j
public class SearchProductHandler {

    @Reference
    private ISearchService searchService;

    @RabbitHandler
    public void process(Long id){
        log.info("处理了id为：{}的消息",id);
        //更新solr索引库
        searchService.updateById(id);
    }
}
