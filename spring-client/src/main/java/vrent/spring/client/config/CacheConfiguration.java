package vrent.spring.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {
  @Bean
  @ConditionalOnMissingBean(CacheManager.class)
  public CacheManager noOpCacheManager() {
    return new NoOpCacheManager();
  }
}
