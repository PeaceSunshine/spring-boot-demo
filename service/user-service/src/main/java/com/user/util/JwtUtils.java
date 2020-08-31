package com.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @Author lx
 * @Date 2020/8/19
 * @Description jwt令牌工具类
 **/
public class JwtUtils {

    /**
     * 密钥由调用方决定
    * */
    private String secretKey;

    /**
     * 有效期也由调用方决定
     * */
    private long ttl;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String createJwtToken(String id,String subjece){
        long now = System.currentTimeMillis();
        // setIssuedAt:设置生成token的时间
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(subjece).setIssuedAt(new Date(now))
                .signWith(SignatureAlgorithm.HS256, secretKey);
        if (ttl>0){
            //过期时间大于0,则设置令牌过期时间为:生成令牌时间+过期时间
            jwtBuilder.setExpiration(new Date(now+ttl));
        }
        return jwtBuilder.compact();
    }

    public Claims parseJwtToken(String jwtToken){
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(jwtToken).getBody();
    }
}
