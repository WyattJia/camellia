package com.netease.nim.camellia.redis.samples;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestUnixDomainSocket {

    public static void main(String[] args) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "/tmp/redis.sock", 2000, 1200,"pass123",
                0, true, null, null, (s, sslSession) -> true);
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get("k1");
            System.out.println(value);
        }
    }
}
