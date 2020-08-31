package com.example;

import com.example.handler.ApiHandler;
import com.example.handler.FailureHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final Router router = Router.router(vertx);

    router.route("/api/*")
      //.consumes("application/json").consumes("text/html") // match contentType within request in RouteState
      .produces("application/json")// match accept within request in RouteState
      .handler(ctx->{
        ctx.response().putHeader("content-type", "application/json");
        ctx.next();
      })
      .handler(ApiHandler.create(vertx, router));

    router.route()
      .failureHandler(FailureHandler.GetFailureHandler());

    vertx.createHttpServer()
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

  public static void main(String[] args) {
    String verticleID = MainVerticle.class.getName();
    VertxOptions options = new VertxOptions();

    Vertx vertx = Vertx.vertx(options);
    vertx.deployVerticle(verticleID);
  }
}
