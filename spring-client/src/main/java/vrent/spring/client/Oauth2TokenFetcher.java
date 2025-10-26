package vrent.spring.client;

import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import vrent.spring.client.utils.Constants;

@Component
public class Oauth2TokenFetcher {
  @Cacheable(value = Constants.TOKEN_CACHE_KEY, key = "#p1")
  @Nonnull
  public String getToken(String tokenUrl, String clientId, String secret) {
    return "this-is-oauth2-token-with-client-id-" + clientId;
  }
}
