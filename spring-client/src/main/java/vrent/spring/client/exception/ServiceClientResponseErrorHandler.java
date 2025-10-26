package vrent.spring.client.exception;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

public class ServiceClientResponseErrorHandler implements RestClient.ResponseSpec.ErrorHandler {
  @Override
  public void handle(HttpRequest request, ClientHttpResponse response) throws IOException {
    throw new ServiceClientException(response.getStatusCode().value(), response.getStatusText());
  }
}
