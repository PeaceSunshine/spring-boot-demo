package email.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.email.IEmailService;
import common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author lx
 * @Date 2020/6/23
 * @Description 实现有一个批量发送邮件的方式，使用多线程
 **/
@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${email.server}")
    private String emailServer;

    /**
     * 发送生日祝福
     * @param to 收件人
     * @param username 用户名
     * @return
     */
    @Override
    public ResultBean sendBirthday(String to, String username) {
        //1.构建邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(emailServer);
            helper.setTo(to);
            //设置邮件标题
            helper.setSubject("【测试项目】生日祝福");
            //邮件内容由模板来生成
            Context context = new Context();
            context.setVariable("username",username);
            //birthday为html模板,使用context对象填充数据
            String content = templateEngine.process("birthday",context);

            helper.setText(content,true);
            //2.发送邮件
            javaMailSender.send(mimeMessage);
            //3.发送邮件
            return ResultBean.success("ok");
        } catch (MessagingException e) {
            e.printStackTrace();
            //第一次失败，添加一条记录
            //第二次失败，更新该记录的重试次数
        }
        return ResultBean.error("邮件发送失败");
    }

    /**
     * 发送激活邮件
     * @param to 收件人
     * @param username 用户名
     * @return
     */
    @Override
    public ResultBean sendActive(String to, String username) {
        return null;
    }
}
