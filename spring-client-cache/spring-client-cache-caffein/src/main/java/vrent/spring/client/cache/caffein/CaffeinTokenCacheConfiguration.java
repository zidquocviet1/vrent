package vrent.spring.client.cache.caffein;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeinTokenCacheConfiguration {
  @Bean
  public CacheManager inMemoryCacheManager() {
    final SimpleCacheManager manager = new SimpleCacheManager();

    final CaffeineCache tokenCache =
        new CaffeineCache(
            "token-cache",
            Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(30, TimeUnit.MINUTES).build());

    manager.setCaches(List.of(tokenCache));

    return manager;
  }
}
