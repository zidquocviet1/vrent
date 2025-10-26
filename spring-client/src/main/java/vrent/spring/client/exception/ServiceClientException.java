package vrent.spring.client.exception;

public class ServiceClientException extends RuntimeException {
  private final int statusCode;

  private final String response;

  public ServiceClientException(int statusCode, String response) {
    this.statusCode = statusCode;
    this.response = response;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getResponse() {
    return response;
  }
}
