package vrent.spring.client.config;

import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import vrent.spring.client.Oauth2TokenFetcher;
import vrent.spring.client.exception.ServiceClientResponseErrorHandler;
import vrent.spring.client.interceptors.BasicAuthTokenInterceptor;
import vrent.spring.client.interceptors.ClientCredentialTokenInterceptor;
import vrent.spring.client.interceptors.RequestLoggingInterceptor;

@Configuration
@Import({CacheConfiguration.class, ServiceClientConfiguration.class, Oauth2TokenFetcher.class})
public class HttpClientConfiguration {
  @Bean
  public RestClientHttpServiceGroupConfigurer configurer(
      final ServiceClientConfiguration serviceClientConfiguration,
      final Oauth2TokenFetcher oauth2TokenFetcher,
      final BeanFactory beanFactory) {
    final ServiceClientResponseErrorHandler errorHandler = new ServiceClientResponseErrorHandler();

    return groups -> {
      for (Map.Entry<String, ServiceClientConfiguration.ClientConfig> serviceIdAndClient :
          serviceClientConfiguration.getClients().entrySet()) {
        final String serviceId = serviceIdAndClient.getKey();
        final ServiceClientConfiguration.ClientConfig client = serviceIdAndClient.getValue();
        final ServiceClientConfiguration.AuthConfig common = serviceClientConfiguration.getCommon();

        groups
            .filterByName(serviceId)
            .forEachGroup(
                (group, clientBuilder, factoryBuilder) ->
                    clientBuilder
                        .baseUrl(client.getBaseUrl())
                        .requestInterceptors(
                            interceptors -> {
                              final ClientHttpRequestInterceptor authenticationRequestInterceptor =
                                  getAuthenticationRequestInterceptor(
                                      common, client, oauth2TokenFetcher, beanFactory);
                              interceptors.add(0, authenticationRequestInterceptor);
                              interceptors.add(1, new RequestLoggingInterceptor());
                            })
                        .defaultStatusHandler(HttpStatusCode::isError, errorHandler));
      }
    };
  }

  private static ClientHttpRequestInterceptor getAuthenticationRequestInterceptor(
      final ServiceClientConfiguration.AuthConfig common,
      final ServiceClientConfiguration.ClientConfig client,
      final Oauth2TokenFetcher oauth2TokenFetcher,
      final BeanFactory beanFactory) {
    if (client.getTokenInterceptor() != null) {
      final Object customTokenInterceptor = beanFactory.getBean(client.getTokenInterceptor());
      if (customTokenInterceptor instanceof ClientHttpRequestInterceptor) {
        return (ClientHttpRequestInterceptor) customTokenInterceptor;
      } else {
        throw new RuntimeException(
            "Token interceptor class is invalid, class should a child of ClientHttpRequestInterceptor");
      }
    }

    try {
      return getClientInterceptor(client, oauth2TokenFetcher);
    } catch (RuntimeException e) {
      return getClientInterceptor(common, oauth2TokenFetcher);
    }
  }

  private static ClientHttpRequestInterceptor getClientInterceptor(
      final ServiceClientConfiguration.AuthConfig authConfig,
      final Oauth2TokenFetcher oauth2TokenFetcher) {
    if (authConfig == null) {
      throw new RuntimeException("Auth config is null");
    }

    final ServiceClientConfiguration.TokenType tokenType = authConfig.getTokenType();
    return switch (tokenType) {
      case ServiceClientConfiguration.TokenType.BASIC ->
          createBasicAuthTokenInterceptor(authConfig);
      default -> createClientCredentialTokenInterceptor(authConfig, oauth2TokenFetcher);
    };
  }

  private static ClientCredentialTokenInterceptor createClientCredentialTokenInterceptor(
      final ServiceClientConfiguration.AuthConfig authConfig,
      final Oauth2TokenFetcher oauth2TokenFetcher) {
    final String tokenUrl = authConfig.getTokenUrl();
    final String clientId = authConfig.getClientId();
    final String secret = authConfig.getSecret();

    if (tokenUrl == null || clientId == null || secret == null) {
      throw new RuntimeException(
          "Missing token and client id and secret for client credentials flow");
    }

    return new ClientCredentialTokenInterceptor(tokenUrl, clientId, secret, oauth2TokenFetcher);
  }

  private static BasicAuthTokenInterceptor createBasicAuthTokenInterceptor(
      final ServiceClientConfiguration.AuthConfig authConfig) {
    if (authConfig.getUsername() == null || authConfig.getPassword() == null) {
      throw new RuntimeException("Missing username and password for basic authentication flow");
    }

    return new BasicAuthTokenInterceptor(authConfig.getUsername(), authConfig.getPassword());
  }
}
