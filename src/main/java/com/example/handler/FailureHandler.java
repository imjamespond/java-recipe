package com.example.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;

public class FailureHandler {
  static public Handler<RoutingContext> GetFailureHandler(){
    return frc -> {
      int statusCode = frc.statusCode();
      Throwable failure = frc.failure();
      String error = "Sorry! Not today";
      if (failure != null) {
        if (failure instanceof HttpStatusException) {
          error = ((HttpStatusException) failure).getPayload();
        }
        else if (error != failure.getMessage()){
          error = failure.getMessage();
        }
      }
      // Status code will be 500 for the RuntimeException, or 403 for the other failure
      frc.response().setStatusCode(statusCode).end(error);
    };
  }
}
