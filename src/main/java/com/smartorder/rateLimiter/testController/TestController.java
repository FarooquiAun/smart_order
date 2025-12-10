package com.smartorder.rateLimiter.testController;

import com.smartorder.rateLimiter.annotations.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RateLimit(limit = 5,durationSeconds = 5)
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
