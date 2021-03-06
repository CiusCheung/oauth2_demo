package com.cius.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
public class AccessTokenConfig {

    private String SIGNING_KEY = "javaboy";

//    @Autowired
//    RedisConnectionFactory redisConnectionFactory;

    /**
     * TokenStore 的实例，这个是指生成的 Token 要往哪里存储
     * @return
     */
    @Bean
    TokenStore tokenStore() {

        //1.内存方式
        //return new InMemoryTokenStore();

        //2.redis方式
        //return new RedisTokenStore(redisConnectionFactory);

        /**
         * 3.Jwt(Json Web Token)方式
         * TokenStore 我们使用 JwtTokenStore 这个实例。
         * 之前我们将 access_token 无论是存储在内存中，还是存储在 Redis 中，都是要存下来的，
         * 客户端将 access_token 发来之后，我们还要校验看对不对。
         * 但是如果使用了 JWT，access_token 实际上就不用存储了（无状态登录，服务端不需要保存信息），
         * 因为用户的所有信息都在 jwt 里边，所以这里配置的 JwtTokenStore 本质上并不是做存储。
         *
         * JWT 交互流程
         *    应用程序或客户端向授权服务器请求授权
         *    获取到授权后，授权服务器会向应用程序返回访问令牌
         *    应用程序使用访问令牌来访问受保护资源（如API）
         * 因为 JWT 签发的 token 中已经包含了用户的身份信息，并且每次请求都会携带，这样服务的就无需保存用户信息，甚至无需去数据库查询，这样就符合了 RESTful 的无状态规范。
         *
         * password模式访问：
         * http://localhost:8080/oauth/token?client_id=javaboy&client_secret=123&grant_type=password&username=sang&password=123
         */
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 提供了一个 JwtAccessTokenConverter，
     * 这个 JwtAccessTokenConverter 可以实现将用户信息和 JWT
     * 进行转换（将用户信息转为 jwt 字符串，或者从 jwt 字符串提取出用户信息）。
     * @return
     */
    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }
}
