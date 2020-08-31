package com.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.user.IUserService;
import com.entity.TUser;
import com.mapper.TUserMapper;
import com.user.util.JwtUtils;
import common.base.BaseServiceImpl;
import common.base.IBaseDao;
import common.pojo.ResultBean;
import common.pojo.TResultBean;
import common.util.CodeUtils;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author lx
 * @Date 2020/8/19
 * @Description
 **/
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {

    @Autowired
    private TUserMapper userMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultBean checkUserNameIsExists(String username) {
        return null;
    }

    @Override
    public ResultBean checkPhoneIsExists(String phone) {
        return null;
    }

    @Override
    public ResultBean checkEmailIsExists(String email) {
        return null;
    }

    @Override
    public ResultBean generateCode(String identification) {
        //1.生成验证码
        String code = CodeUtils.generateCode(6);
        //2.往redis中保存一个凭证和验证码的对应关系
        redisTemplate.opsForValue().set(identification,code,2, TimeUnit.MINUTES);
        /*
        * 3.发送短信验证码
        * 3.1 调通阿里云提供的短信demo
        * 3.2 将发送短信抽取成一个公共服务
        * 3.3 发送消息，异步处理发送短信
        * */
        Map<String,String> params = new HashMap<>();
        params.put("identification",identification);
        params.put("code",code);
        rabbitTemplate.convertAndSend("sms-exchange","sms.code",params);

        //此处不需要发送消息，仅做测试
        Map<String,String> params2 = new HashMap<>();
        params2.put("to","1980526605@qq.com");
        params2.put("username","林爱青");
        rabbitTemplate.convertAndSend("email-exchange","email.birthday",params);
        return ResultBean.success("ok");
    }

    @Override
    public TResultBean checkLogin(TUser user) {
        //1.根据手机/邮箱查询用户
        TUser currentUser = userMapper.selectByIdentification(user.getUsername());
        if(currentUser.getPassword()!=null){
            if(passwordEncoder.matches(user.getPassword(),currentUser.getPassword())){
                /*
                * 账号密码匹配
                * 1.生成uuid
                * String uuid = UUID.randomUUID().toString();
                * 2.保存到redis中，设置有效期为30分钟，取代原先的session
                * redisTemplate.opsForValue().set("user:token:"+uuid,currentUser.getUsername(),30,TimeUnit.MINUTES);
                * */

                //生成令牌
                JwtUtils jwtUtils = new JwtUtils();
                jwtUtils.setSecretKey("LXXCX");
                jwtUtils.setTtl(30*60*1000);

                String jwtToken = jwtUtils.createJwtToken(currentUser.getId().toString(),currentUser.getUsername());
                //构建一个map,返回令牌和唯一标识
                Map<String, String> params = new HashMap<>();
                params.put("jwtToken",jwtToken);
                params.put("username",currentUser.getUsername());
                return new TResultBean("200",params);
            }
        }

        return new TResultBean("404",null);
    }

    @Override
    public ResultBean checkIsLogin(String jwtToken) {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setSecretKey("LXXCX");
        //解析令牌
        try {
            Claims claims = jwtUtils.parseJwtToken(jwtToken);
            String username = claims.getSubject();
            return ResultBean.success(username);
        }catch (Exception e){
            return ResultBean.error(e.getMessage());
        }
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}
