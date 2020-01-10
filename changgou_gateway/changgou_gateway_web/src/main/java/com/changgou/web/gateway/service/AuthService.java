package com.changgou.web.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从cookie中获取jti的值
     *
     * @param request
     * @return
     */
    public String getJtiFromCookie(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst("uid");
        if (cookie != null) {
            String jti = cookie.getValue();
            return jti;
        }
        return null;
    }

    /**
     * 从redis中获取jwt令牌
     *
     * @param jti
     * @return
     */
    public String getJwtFromRedis(String jti) {
        String jwt = stringRedisTemplate.boundValueOps(jti).get();
        return jwt;
    }
}
