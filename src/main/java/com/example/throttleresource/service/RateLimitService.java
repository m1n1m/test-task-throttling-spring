package com.example.throttleresource.service;

public interface RateLimitService {

    boolean isAllowedFromIP(final String ipAddress);
}
