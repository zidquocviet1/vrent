package vrent.spring.client.interceptors;

import java.io.IOException;
import java.util.Base64;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class BasicAuthTokenInterceptor implements ClientHttpRequestInterceptor {
  private final String username;

  private final String password;

  public BasicAuthTokenInterceptor(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    request
        .getHeaders()
        .add(
            "Authorization",
            "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
    return execution.execute(request, body);
  }
}
