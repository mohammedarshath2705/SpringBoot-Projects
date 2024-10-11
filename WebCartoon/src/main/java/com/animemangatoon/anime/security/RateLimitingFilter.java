package com.animemangatoon.anime.security;

import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Integer> requestCounts = ExpiringMap.builder()
            .expiration(Duration.ofMinutes(1))
            .build();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        int count = requestCounts.getOrDefault(ipAddress, 0);

        if (count >= 100) { // Limit requests to 100 per minute
            response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
            response.getWriter().write("Too many requests");
            return;
        }

        requestCounts.put(ipAddress, count + 1);
        chain.doFilter(request, response);
    }
}
