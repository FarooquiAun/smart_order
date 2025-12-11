package com.smartorder.rateLimiter.testController;

import com.smartorder.rateLimiter.annotations.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RateLimit(limit = 10,durationSeconds = 60)
    @GetMapping("/v1/token-test")
    public String hello(){
        return "Hello World";
    }
    @RateLimit(limit = 5, durationSeconds = 10, algorithm = "sliding-redis")
    @GetMapping("/v1/redis-test")
    public String redisTest() {
        return "Redis Sliding Window Works!";
    }

}
