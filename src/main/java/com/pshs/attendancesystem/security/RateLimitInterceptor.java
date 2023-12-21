package com.pshs.attendancesystem.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;

public class RateLimitInterceptor implements HandlerInterceptor {

	private final ConcurrentHashMap<String, Long> requestsTimeStamp = new ConcurrentHashMap<>();
	private final Logger logger = LogManager.getLogger(this.getClass());

	private int requestPerMinute = 60;
	private final long cleanUpInterval = 1000 * 30; // Every 30 seconds
	private final long timeInterval = 1000 * 60; // Every minute

	public RateLimitInterceptor(Integer requestPerMinute) {
		this.requestPerMinute = requestPerMinute;
	}

	public RateLimitInterceptor() {

	}

	@Scheduled(fixedRate = cleanUpInterval)
	private void removeExpiredEntries() {
		long currentTime = System.currentTimeMillis();

		logger.info("Removing expired rate limited entries.");
		requestsTimeStamp.entrySet()
			.removeIf(entry -> currentTime - entry.getValue() > timeInterval);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String clientIp = getClientIp(request);

		if (isRateLimited(clientIp)) {
			response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
			return false;
		}

		// Save the timestamp of the request.
		requestsTimeStamp.put(clientIp, System.currentTimeMillis());
		return true;
	}

	private String getClientIp(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private boolean isRateLimited(String clientId) {
		// Check if the client exceeded the rate limit.
		Long lastRequest = requestsTimeStamp.get(clientId);

		if (lastRequest != null) {
			// Check if the client is in the rate limit.
			long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - lastRequest;

			return elapsedTime < timeInterval && requestsTimeStamp.size() >= requestPerMinute;
		}

		return false;
	}
}
