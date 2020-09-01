package com.sd.util;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.http.HttpServerRequest;

public class RequestWrapper {

  private final HttpServerRequest request;

  public RequestWrapper(HttpServerRequest request) {
    this.request = request;
  }

  public <T> T getWithDefault(String param, T defaultVal) {
    T val = (T) request.getParam(param);
    return val == null ? defaultVal : val;
  }
}
