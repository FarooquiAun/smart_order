package com.smartorder.rateLimiter.redisRateLimiter;

import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RedisSlidingWindowRateLimiter {
    @Autowired
    private StringRedisTemplate redis;
    private final DefaultRedisScript<Long> script;

    public RedisSlidingWindowRateLimiter() {
       String lua= """
               local key=KEYS[1]
               local now=tonumber(ARGV[1])
               local window=tonumber(ARGV[2])
               local limit=tonumber(ARGV[3])
               local windowStart=now -window
               
               --Removing old entries outside of window
               redis.call("ZREMRANGEBYSCORE",key,0,windowStart)
                -- Add current request timestamp
               redis.call("ZADD", key, now, now)
               
               -- Count total requests in window
               local count = redis.call("ZCARD", key)
             
                return count
               """;

        this.script = new DefaultRedisScript<>(lua,Long.class);
    }
    public boolean allowRequest(String key,int limit,int windowSecond){
        long now =System.currentTimeMillis();

        Long count=redis.execute(
                script,
                Collections.singletonList("rate-limit:"+key),
                String.valueOf(now),
                String.valueOf(windowSecond*1000),
                String.valueOf(limit)
        );
        return count<=limit;
    }
}
