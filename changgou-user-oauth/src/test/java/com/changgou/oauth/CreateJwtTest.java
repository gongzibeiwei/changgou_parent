package com.changgou.oauth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.HashMap;
import java.util.Map;

public class CreateJwtTest {

    @Test
    public void createJWT() {
        //基于私钥生成JWT令牌
        //1.创建密钥工厂
        //参数1：指定私钥位置，参数2：指定密钥库的密码
        ClassPathResource classPathResource = new ClassPathResource("changgou.jks");
        String keyPass = "changgou";
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keyPass.toCharArray());
        //2.基于工厂获取私钥
        String alias = "changgou";
        String password = "changgou";
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, password.toCharArray());
        //将当前的私钥转换为rsa私钥
        RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) keyPair.getPrivate();
        //3.生成JWT令牌
        Map<String, String> map = new HashMap();
        map.put("company", "heima");
        map.put("address", "beijing");
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(map), new RsaSigner(rsaPrivateCrtKey));
        String jwtEncoded = jwt.getEncoded();
        System.out.println(jwtEncoded);
    }
}
