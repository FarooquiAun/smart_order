package com.smartorder.rateLimiter.Limiter;

public class TokenBucket {
    private final long capacity;
    private final double refillRatePerSec;
    private double availableTokens;
    private long lastRefillTimeStamp;

    public TokenBucket(long capacity, double refillRatePerSec) {
        this.capacity = capacity;
        this.refillRatePerSec = refillRatePerSec;
        this.availableTokens=capacity;
        this.lastRefillTimeStamp=System.currentTimeMillis();
    }

    public synchronized boolean tryConsume(){
        refill();
        if (availableTokens>=1){
            availableTokens=-1;
            return true;
        }
        return false;
    }
    public void refill(){
        long now=System.currentTimeMillis();
        if (now<=lastRefillTimeStamp){
            return;
        }
        long elapsedtime=now-lastRefillTimeStamp;
        double tokenToAdd=(elapsedtime/1000.0)*refillRatePerSec;
        if (tokenToAdd>0){
            availableTokens=Math.min(capacity,availableTokens+tokenToAdd);
            lastRefillTimeStamp=now;
        }
    }

}
