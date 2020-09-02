package com.example.handler;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.InvocationTargetException;

public class GenericHandler<I> {
  I proxy;
  GenericHandler(){
    proxy = (I) java.lang.reflect.Proxy.newProxyInstance(
      this.getClass().getClassLoader(),
      this.getClass().getInterfaces(),
      (proxy, method, args) -> {
        Object result;
        try {
          System.out.println("before method " + method.getName());
          RoutingContext ctx = (RoutingContext) args[0];
          result = method.invoke(this, args);
          if (result != null) {
            ctx.response().end(Json.encodePrettily(result));
          }
        } catch (InvocationTargetException e) {
          throw e.getTargetException();
        } catch (Exception e) {
          throw new RuntimeException("unexpected invocation exception: " +
            e.getMessage());
        } finally {
          System.out.println("after method " + method.getName());
        }
        return null;
      });
  }
}
