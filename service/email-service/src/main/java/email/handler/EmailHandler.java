package email.handler;

import com.api.email.IEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author lx
 * @Date 2020/6/23
 * @Description 处理邮件服务
 **/
@Component
public class EmailHandler {

    @Autowired
    private IEmailService emailService;

    @RabbitListener(queues = "email-birthday")
    @RabbitHandler
    public void processSendBirthday(Map<String,String> params){
        String to =params.get("to");
        String username = params.get("username");
        emailService.sendBirthday(to,username);
    }
}
