package com.example.throttleresource.service;

import com.example.throttleresource.config.ThrottleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
//@Primary
public class RateLimitServiceFixedTimeWindowImpl implements RateLimitService {

    private final ThrottleConfig throttleConfig;

    private final ConcurrentHashMap<String, FixedTimeWindowsStruct> requestsHistory = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowedFromIP(final String ipAddress) {
        final FixedTimeWindowsStruct history = requestsHistory.computeIfAbsent(ipAddress, s -> new FixedTimeWindowsStruct());
        final boolean isAllowed = history.isAllowed(throttleConfig.getDuration(), throttleConfig.getLimit());
        if (log.isDebugEnabled()) {
            log.debug("Client IP {}\t requests count {}\t {}", ipAddress, history.requestsCount.get(), isAllowed);
        }
        return isAllowed;
    }

    static class FixedTimeWindowsStruct {
        long createdTs = System.currentTimeMillis();
        AtomicInteger requestsCount = new AtomicInteger(0);

        public void reset()
        {
            createdTs = System.currentTimeMillis();
            requestsCount.set(0);
        }

        void invalidate(final Duration duration)
        {
            if (Instant.ofEpochMilli(createdTs).plus(duration).isBefore(Instant.now()))
            {
                reset();
            }
        }

        public boolean isAllowed(final Duration duration, final int limit) {
            invalidate(duration);
            return requestsCount.incrementAndGet() <= limit;
        }
    }
}
