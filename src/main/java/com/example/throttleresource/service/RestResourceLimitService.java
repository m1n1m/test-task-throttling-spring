package com.example.throttleresource.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class RestResourceLimitService {

    private final RateLimitService rateLimitService;

    public RestResourceLimitService(final RateLimitService rateLimitService)
    {
        this.rateLimitService = rateLimitService;
    }

    public boolean isAllowed(final HttpServletRequest request) {
        final String clientIp = request.getRemoteAddr();
        return rateLimitService.isAllowedFromIP(clientIp);
    }
}
