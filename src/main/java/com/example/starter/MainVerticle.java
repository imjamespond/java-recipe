package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

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

    // Set a static server to serve static resources, e.g. the login page
    router.route(HttpMethod.GET, "/static/*").order(1).handler(StaticHandler.create());
    // The BodyHandler allows you to retrieve request bodies, limit body sizes and handle file uploads.
    router.route().order(0).handler(BodyHandler.create().setBodyLimit(1024l));

    SessionStore store = LocalSessionStore.create(vertx);
    SessionHandler sessionHandler = SessionHandler
      .create(store)
      .setSessionTimeout(50000l);//5 seconds
    // Make sure all requests are routed through the session handler too
    router.route().handler(sessionHandler);

    router.route("/session/*")/*.consumes("application/json")*/
      .produces("application/json")
      .handler(com.example.starter.SessionHandler.create(vertx, router));

    router.route().failureHandler(frc -> {
      int statusCode = frc.statusCode();
      Throwable failure = frc.failure();
      String error = "Sorry! Not today";
      if (failure != null) {
        if (failure instanceof HttpStatusException) {
          error = ((HttpStatusException) failure).getPayload();
        }
        if (error == null){
          error = failure.getMessage();
        }
      }
      // Status code will be 500 for the RuntimeException, or 403 for the other failure
      frc.response().setStatusCode(statusCode).end(error);
    });

    router.get("/hello/:productID").handler(ctx -> {
      String productID = ctx.request().getParam("productID");
      System.out.printf("productID: %s\n", productID);
        ctx.response().setChunked(true).write("Hello World");
        ctx.next();
      }
    ).blockingHandler(ctx -> {
      sleep(3000l);
      ctx.response().putHeader("content-type", "text/html").end();
    });


    vertx
      .createHttpServer()
      .requestHandler(router)
//      .requestHandler(req -> {
//        req.response()
//          .putHeader("content-type", "text/plain")
//          .end("Hello from Vert.x!");
//      })
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
