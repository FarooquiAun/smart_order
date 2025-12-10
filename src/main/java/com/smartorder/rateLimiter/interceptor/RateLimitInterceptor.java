package com.smartorder.rateLimiter.interceptor;

import com.smartorder.rateLimiter.Limiter.TokenRateLimiter;
import com.smartorder.rateLimiter.annotations.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenRateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod method=(HandlerMethod) handler;
        RateLimit annotation=method.getMethod().getAnnotation(RateLimit.class);
        if (annotation==null) return true;
        int limit=annotation.limit();
        int duration=annotation.durationSeconds();
        String clientKey= request.getLocalAddr();
        boolean allowed=rateLimiter.allowRequest(clientKey,limit,duration);
        if (!allowed) {
            response.sendError(429, "Too Many Requests");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
