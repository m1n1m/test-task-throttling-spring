package com.example.throttleresource.service;

import com.example.throttleresource.config.ThrottleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class RateLimitServiceSlidingTimeWindowImpl implements RateLimitService {

    private final ThrottleConfig throttleConfig;

    private final ConcurrentHashMap<String, List<Long>> requestsHistory = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowedFromIP(final String ipAddress) {

        final long currentTs = System.currentTimeMillis();
        final long timeWindowBegin = currentTs - throttleConfig.getDuration().toMillis();
        final var history = requestsHistory.computeIfAbsent(ipAddress, s -> new ArrayList<>())
                                           .stream()
                                           .filter(ts -> ts > timeWindowBegin)
                                           .collect(Collectors.toList());

        history.add(currentTs);
        requestsHistory.put(ipAddress, history);
        final int requestsCount = history.size();
        final boolean isAllowed = requestsCount <= throttleConfig.getLimit();

        if (log.isDebugEnabled()) {
            log.debug("Client IP {}\t requests count {}\t {}", ipAddress, requestsCount, isAllowed);
        }
        return isAllowed;
    }
}
