package com.pshs.attendancesystem.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Value("${api.root}")
	private String baseUrl;
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RateLimitInterceptor(10))
			.addPathPatterns(baseUrl + "/**");
		registry.addInterceptor(new RateLimitInterceptor(5))
			.addPathPatterns("/auth/**");
		logger.info("Rate Limiter Interceptor successfully initialized.");
	}
}
