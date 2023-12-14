package com.pshs.attendancesystem.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CachingConfiguration {

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager(
			"attendance",
			"student",
			"fingerprint",
			"rfid_credentials",
			"guardian",
			"section",
			"subject",
			"teacher",
			"strand",
			"gradelevel"
		);
		cacheManager.setCaffeine(caffeineCacheBuilder());

		return cacheManager;
	}

	private Caffeine<Object, Object> caffeineCacheBuilder() {
		return Caffeine.newBuilder()
			.expireAfterWrite(60, TimeUnit.MINUTES)
			.maximumSize(1000)
			.recordStats();
	}
}