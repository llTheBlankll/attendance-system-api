package com.pshs.attendancesystem.config;

import com.pshs.attendancesystem.impl.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfiguration {

	private final ConfigurationService configurationService;
	private final Logger logger = LogManager.getLogger(this.getClass());

	public RedisConfiguration(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(configurationService.getRedisHost());
		redisStandaloneConfiguration.setDatabase(0);
		redisStandaloneConfiguration.setPort(configurationService.getRedisPort());
		redisStandaloneConfiguration.setPassword(configurationService.getRedisPassword());
		logger.info("Redis Host: " + redisStandaloneConfiguration.getHostName());
		logger.info("Redis Port: " + redisStandaloneConfiguration.getPort());
		logger.info("Redis Successful Connection");
		return new JedisConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(5));

		logger.info("Redis Cache Configuration successfully set");
		return RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(redisCacheConfiguration)
			.build();
	}
}
