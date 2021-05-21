package com.cius.auth.controller;

import com.cius.auth.task.TokenTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TokenTask tokenTask;

    /**
     * 如果 code 不为 null，也就是如果是通过授权服务器重定向到这个地址来的，那么我们做如下两个操作：
     * 根据拿到的 code，去请求 http://localhost:8080/oauth/token 地址去获取
     * @param code
     * @param model
     * @return
     */
    @GetMapping("/index.html")
    public String hello(String code, Model model) {
//        if (code != null) {
//            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//            map.add("code", code);
//            map.add("client_id", "javaboy");
//            map.add("client_secret", "123");
//            map.add("redirect_uri", "http://localhost:8082/index.html");
//            map.add("grant_type", "authorization_code");
//            Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
//            String access_token = resp.get("access_token");
//            System.out.println(access_token);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", "Bearer " + access_token);
//            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
//            ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
            model.addAttribute("msg", tokenTask.getData(code));
//        }
        return "index";
    }

    @PostMapping("/login")
    public String login(String username, String password,Model model) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("client_secret", "123");
        map.add("client_id", "javaboy");
        map.add("grant_type", "password");
        Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
        String access_token = resp.get("access_token");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8081/admin/hello", HttpMethod.GET, httpEntity, String.class);
        model.addAttribute("msg", entity.getBody());
        return "index";
    }
}
