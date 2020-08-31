package com.config;

import common.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lx
 * @Date 2020/6/23
 * @Description
 **/
@Configuration
public class RabbitConfig {

    /**
     * 1.声明队列
     * @return
     */
    @Bean
    public Queue initProductSearchQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.CENTER_PRODUCT_EXCHANGE_SEARCH_QUEUE, true, false,false);
        return  queue;

    }

    /**
     * 2.声明交换机
     * @return
     */
    @Bean
    public TopicExchange initProductExchange(){
        TopicExchange productExchange = new TopicExchange(
                MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,true,false);
        return productExchange;

    }

    /**
     * 3.绑定交换机
     * @return
     */
    @Bean
    public Binding bindProductSearchQueueToProductExchange(Queue initProductSearchQueue,TopicExchange initProductExchange){
       return BindingBuilder.bind(initProductSearchQueue).to(initProductExchange).with("product.add");

    }


}
