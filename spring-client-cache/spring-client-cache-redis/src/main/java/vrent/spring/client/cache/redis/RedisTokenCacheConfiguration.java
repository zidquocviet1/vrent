package vrent.spring.client.cache.redis;

import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTokenCacheConfiguration {
  //  @Bean
  //  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
  //    return builder ->
  //        builder.withCacheConfiguration(
  //            "redis-token-cache",
  //            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(55)));
  //  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .entryTtl(Duration.ofMinutes(10));
  }

  @Bean
  @ConditionalOnBean(RedisConnectionFactory.class)
  public CacheManager redisCacheManager(
      RedisConnectionFactory connectionFactory, RedisCacheConfiguration redisCacheConfiguration) {
    final RedisCacheConfiguration tokenCacheConfig =
        redisCacheConfiguration.entryTtl(Duration.ofMinutes(30));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(redisCacheConfiguration)
        .withCacheConfiguration("token-cache", tokenCacheConfig)
        .transactionAware()
        .build();
  }
}
