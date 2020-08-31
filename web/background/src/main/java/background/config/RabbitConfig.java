package background.config;

import common.constant.MQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lx
 * @Date 2020/6/23
 * @Description MQ配置
 **/
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initProductExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE, true, false);
        return  topicExchange;

    }
}
