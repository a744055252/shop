package com.juan.shop.token;

import com.juan.shop.spring.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.DateUtils;
import util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author liguanhuan
 */
@Service
@Slf4j
public class JwtTokenManager implements TokenManager{

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String createToken(String username) {

        long now = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setId(username)
                .setSubject(username)
                //设置签发时间
                .setIssuedAt(new Date())
                //设置签名秘钥
                .signWith(SignatureAlgorithm.HS256, tokenProperties.getSignKey());

        String token = builder.compact();

        redisTemplate.opsForValue().set(token, username, tokenProperties.getExpired(), TimeUnit.MINUTES);

        return token;
    }

    @Override
    public String getUserFromToken(String token) {
        String result = redisTemplate.opsForValue().get(token);
        if (StringUtils.isNotBlank(result)) {
            redisTemplate.opsForValue().set(token, result, tokenProperties.getExpired(), TimeUnit.MINUTES);
        }
        return result;
    }

    @Override
    public void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可。
    }
}