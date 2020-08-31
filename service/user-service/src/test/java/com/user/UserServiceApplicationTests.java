package com.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    public void encodeTest(){
        //MD5 不可逆
        //abc ----> def
        //MD5在线解密
        //建立字典表（原文-密文）
        //加大解密的难度，会加盐

        //abc ---->def
        //abc ---->def

        //abc--->def
        //abc--->dei

        String encode1 = passwordEncoder.encode("123456");
        String encode2 = passwordEncoder.encode("123456");
        System.out.println(encode1);
        System.out.println(encode2);
        //$2a$10$Hxs5VhfcNYfN1ReM0E5xzeNs.9ponDNf4QkGPND5zHkwQB3mrJPYK
        //$2a$10$jlsbLJKBwmSb6PjusUcuZ.ljMYhuvfKF2OMqYMGqa3/UeIB4mqbvm
        //$2a$10$ZUsOoJQOVNUUlldS1zSqd.zxdBFdPTwoAXHpHV8cG6CrTL5Y4YI/a
        //$2a$10$ZKmIBkLTr7L8/ONUzCSh2.58BLhH7yInGUG8SQ/s3iEWvPDMqYmZ6
    }

}
