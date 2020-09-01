package com.sd;

import com.sd.handler.ApiHandler;
import com.sd.handler.FailureHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    String verticleID = MainVerticle.class.getName();
    VertxOptions options = new VertxOptions();

    Vertx vertx = Vertx.vertx(options);
    vertx.deployVerticle(verticleID);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final Router router = Router.router(vertx);

    router.route()
      .handler(ctx -> {
        ctx.response().putHeader("content-type", "application/json; charset=utf-8");
        ctx.next();
      })
      .failureHandler(FailureHandler.GetFailureHandler());

    ApiHandler.create(vertx, router);

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
}
