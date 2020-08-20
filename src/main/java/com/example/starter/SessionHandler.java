package com.example.starter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;
import io.vertx.ext.web.handler.impl.HttpStatusException;

public interface SessionHandler extends Handler<RoutingContext> {
  static SessionHandler create(Vertx vertx, Router router) {
    return new SessionHandlerImpl(vertx, router);
  }
}

class SessionHandlerImpl implements SessionHandler{

  private final Router router;
  private final AuthProvider authProvider;

  public SessionHandlerImpl(Vertx vertx, Router router) {
    this.router = router;
    this.authProvider = new DummyAuthProvider();

    router.route().handler(new DummyAuthHandler(authProvider).addAuthority("role:admin"));
    router.get("/get").handler(this::getSession);
    router.get("/set").handler(this::setSession);
  }

  private void setSession(RoutingContext ctx) {
    Session session = ctx.session();
    session.put("authInfo", ctx.user().principal());

//    authProvider.authenticate(authInfo,res->{
//      if (res.succeeded()) {
//        session.put("authInfo",authInfo);
//        ctx.response().end("OK");
//      } else {
//        ctx.fail(403, res.cause());
//      }
//    });

    ctx.response().end("OK");
  }

  private void getSession(RoutingContext ctx) {
    Session session = ctx.session();
    Object user = session.get("authInfo");
    if(user == null) {
      ctx.fail(403, new Throwable("user info is null"));
    }else {
      ctx.response().end(user.toString());
    }
  }

  @Override
  public void handle(RoutingContext ctx) {
    router.handleContext(ctx);
  }

}

class DummyAuthProvider implements AuthProvider {

  private String storedUsername = "foobar";
  private String storedPassword = "pwd";

  @Override
  public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
    if (storedUsername == null || storedPassword == null) {
      handler.handle(Future.failedFuture("Credentials not set in configuration."));
      return;
    }

    String username = authInfo.getString("username");
    String password = authInfo.getString("password");

    if (storedUsername.equals(username) && storedPassword.equals(password)) {
      handler.handle(Future.succeededFuture(new UserImpl(authInfo)));
    } else {
      handler.handle(Future.failedFuture("No such user, or password incorrect."));
    }
  }
}

class DummyAuthHandler extends AuthHandlerImpl {
  public DummyAuthHandler(AuthProvider authProvider) {
    super(authProvider);
  }

  @Override
  public void parseCredentials(RoutingContext ctx, Handler<AsyncResult<JsonObject>> handler) {
    Session session = ctx.session();
    JsonObject authInfo = session.get("authInfo");
    if (session != null) {
      if (authInfo == null) {
        String userName = ctx.request().getParam("username");
        String password = ctx.request().getParam("password");
        System.out.printf("username: %s, password: %s\n", userName, password);
        authInfo = new JsonObject()
          .put("username", userName)
          .put("password", password);
      } else {
        ctx.setUser(new UserImpl(authInfo) ); // avoid auth provider authenticate
      }

      handler.handle(Future.succeededFuture(authInfo));
    } else {
      handler.handle(Future.failedFuture(new HttpStatusException(401) ));
      // Now redirect to the login url - we'll get redirected back here after successful login
      //handler.handle(Future.failedFuture(new HttpStatusException(302, "/hello")));
    }
  }
}

class UserImpl extends AbstractUser {

  private JsonObject authInfo;

  public UserImpl(JsonObject authInfo) {
    this.authInfo = authInfo;
  }

  @Override
  protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> resultHandler) {
    System.out.println(permission);
    resultHandler.handle(Future.succeededFuture(true));
  }

  @Override
  public JsonObject principal() {
    System.out.println("principal");
    return authInfo;
  }

  @Override
  public void setAuthProvider(AuthProvider authProvider) {
    System.out.println("setAuthProvider");
  }
}
