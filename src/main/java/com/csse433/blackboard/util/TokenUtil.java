package com.csse433.blackboard.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.csse433.blackboard.common.Constants;
import io.netty.util.HashedWheelTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chetzhang
 **/
@Component
public class TokenUtil {
    //设置过期时间
    private static final long EXPIRE_DATE = 30 * 60 * 100000;
    //token秘钥
    private static final String TOKEN_SECRET = "ZCfasfhuaUUHufguGuwu2020BQWE";



    public static String token(String username) {

        String token = "";
        try {
            //key and encryption algorithm.
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //header information.
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            //Generates signature with username.
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("date", new Date())
                    .withClaim("username", username)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }

    public static boolean verify(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Redis key for user login token.
     *
     * @param token
     * @return
     */
    public static String getLoginTokenKey(String token) {
        return Constants.TOKEN_KEY + token;
    }

//    /**
//     * Set token expire time.
//     *
//     * @param token
//     * @param tokenExpireTime
//     */
//    public static void setTokenExpireTime(String token, long tokenExpireTime) {
//        RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("RedisTemplate");
//        redisTemplate.expire(TokenUtil.getLoginTokenKey(token), tokenExpireTime, TimeUnit.MINUTES);
//    }
}