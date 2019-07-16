package com.pchudzik.blog.example.testcontainers;

import redis.clients.jedis.Jedis;

import java.util.Optional;

public class KeyValueStorage {
    private final Jedis jedis;

    public KeyValueStorage(String host, int port) {
        jedis = new Jedis(host, port);
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(jedis.get(key));
    }
}
