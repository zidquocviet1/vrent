package vrent.spring.client.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "vrent.http")
@Configuration
public class ServiceClientConfiguration {
  private AuthConfig common;

  private Map<String, ClientConfig> clients;

  public Map<String, ClientConfig> getClients() {
    return clients;
  }

  public void setClients(Map<String, ClientConfig> clients) {
    this.clients = clients;
  }

  public AuthConfig getCommon() {
    return common;
  }

  public void setCommon(AuthConfig common) {
    this.common = common;
  }

  public static class AuthConfig {
    private String tokenUrl;

    private String username;

    private String password;

    private String clientId;

    private String secret;

    private TokenType tokenType;

    private TokenCacheType tokenCacheType;

    public AuthConfig() {}

    public AuthConfig(
        String tokenUrl,
        String username,
        String password,
        String clientId,
        String secret,
        TokenType tokenType,
        TokenCacheType tokenCacheType) {
      this.tokenUrl = tokenUrl;
      this.username = username;
      this.password = password;
      this.clientId = clientId;
      this.secret = secret;
      this.tokenType = tokenType;
      this.tokenCacheType = tokenCacheType;
    }

    public String getTokenUrl() {
      return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
      this.tokenUrl = tokenUrl;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }

    public String getSecret() {
      return secret;
    }

    public void setSecret(String secret) {
      this.secret = secret;
    }

    public TokenType getTokenType() {
      return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
      this.tokenType = tokenType;
    }

    public TokenCacheType getTokenCacheType() {
      return tokenCacheType;
    }

    public void setTokenCacheType(TokenCacheType tokenCacheType) {
      this.tokenCacheType = tokenCacheType;
    }
  }

  public enum TokenCacheType {
    IN_MEMORY,
    REDIS
  }

  public enum TokenType {
    BASIC,
    CLIENT_CREDENTIALS
  }

  public static class ClientConfig extends AuthConfig {
    private String baseUrl;

    private Class<?> tokenInterceptor;

    public ClientConfig() {}

    public ClientConfig(String baseUrl, Class<?> tokenInterceptor) {
      this.baseUrl = baseUrl;
      this.tokenInterceptor = tokenInterceptor;
    }

    public ClientConfig(
        String tokenUrl,
        String username,
        String password,
        String clientId,
        String secret,
        TokenType tokenType,
        TokenCacheType tokenCacheType,
        String baseUrl,
        Class<?> tokenInterceptor) {
      super(tokenUrl, username, password, clientId, secret, tokenType, tokenCacheType);
      this.baseUrl = baseUrl;
      this.tokenInterceptor = tokenInterceptor;
    }

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    public Class<?> getTokenInterceptor() {
      return tokenInterceptor;
    }

    public void setTokenInterceptor(Class<?> tokenInterceptor) {
      this.tokenInterceptor = tokenInterceptor;
    }
  }
}
