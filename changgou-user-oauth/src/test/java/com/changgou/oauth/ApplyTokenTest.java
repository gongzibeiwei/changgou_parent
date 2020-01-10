package com.changgou.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplyTokenTest {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Test
    public void applyToken() {
        //构建请求地址http://localhost:9200/oauth/token
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        //http://localhost:9200
        URI uri = serviceInstance.getUri();
        //http://localhost:9200/oauth/token
        String url = uri + "/oauth/token";

        //封装请求参数 boby,headers
        MultiValueMap<String, String> boby = new LinkedMultiValueMap<>();
        boby.add("grant_type", "password");
        boby.add("username", "itheima");
        boby.add("password", "itheima");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", this.getHttpBasic("changgou", "changgou"));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(boby, headers);

        //当后端出现了401，400.后端不对这两个异常进行处理，而是直接返回给前端
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        //发送请求，获取数据
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map map = responseEntity.getBody();
        System.out.println(map);
    }

    /**
     * 对请求头值进行编码
     *
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String getHttpBasic(String clientId, String clientSecret) {
        String value = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic " + new String(encode);
    }
}
