package vrent.spring.client.interceptors;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import vrent.spring.client.Oauth2TokenFetcher;

public class ClientCredentialTokenInterceptor implements ClientHttpRequestInterceptor {
  private final String tokenUrl;

  private final String clientId;

  private final String clientSecret;

  private final Oauth2TokenFetcher oauth2TokenFetcher;

  public ClientCredentialTokenInterceptor(
      String tokenUrl,
      String clientId,
      String clientSecret,
      Oauth2TokenFetcher oauth2TokenFetcher) {
    this.tokenUrl = tokenUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.oauth2TokenFetcher = oauth2TokenFetcher;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    request
        .getHeaders()
        .add(
            "Authorization",
            "Bearer " + oauth2TokenFetcher.getToken(tokenUrl, clientId, clientSecret));

    return execution.execute(request, body);
  }
}
