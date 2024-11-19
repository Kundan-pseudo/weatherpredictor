package com.example.weatherpredictor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {
    @Value("${jedis.host}")
    public String host;

    @Value("${jedis.port}")
    public int port;

    @Bean
    public Jedis jedis() {
        return new Jedis(host, port);
    }
}
