package com.smartorder.rateLimiter.Limiter;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenRateLimiter {
    private  final ConcurrentHashMap<String,TokenBucket> buckets=new ConcurrentHashMap<>();
     public boolean allowRequest(String key,long limit,long windowSeconds){
          long capacity=limit;
          double refillRate=(double) limit/windowSeconds;
         TokenBucket bucket=buckets.computeIfAbsent(
                 key,k->new TokenBucket(capacity,refillRate)
         );
         return bucket.tryConsume();
     }
}
