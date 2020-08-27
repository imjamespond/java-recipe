package com.example.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.impl.AuthorizationHandlerImpl;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

public interface ApiHandler extends Handler<RoutingContext> {
  static ApiHandler create(Vertx vertx, Router router) {
    return new ApiHandlerImpl(vertx, router);
  }
}

class ApiHandlerImpl implements ApiHandler {

  private final Router router;
  private final Authorization authorization;
  private final AuthorizationProvider authProvider;

  public ApiHandlerImpl(Vertx vertx, Router superRouter) {
    this.router = Router.router(vertx);
    this.authProvider = new DummyAuthProvider();
    this.authorization = new DummyAuthorization();

    SessionStore store = LocalSessionStore.create(vertx);
    SessionHandler sessionHandler = SessionHandler
      .create(store)
      .setNagHttps(false)// allow http
      .setSessionTimeout(60000l);// 1 minute

    router.route()
      .handler(sessionHandler);
      //.handler(new DummyAuthHandler(authorization).addAuthorizationProvider(authProvider));

    router.get("/get").handler(this::getSession);
    router.get("/set").handler(this::setSession);
    router.route("/github/*").handler(GithubHandler.create(vertx, router));
    router.route("/oauth2/*").handler(OAuth2Handler.create(vertx, router));

    router.mountSubRouter("/api", superRouter);
  }

  private void setSession(RoutingContext ctx) {
    Session session = ctx.session();
    session.put("authInfo", ctx.user().principal());

    ctx.response().end("OK");
  }

  private void getSession(RoutingContext ctx) {
    Session session = ctx.session();
    Object authInfo = session.get("authInfo");
    if(authInfo == null) {
      ctx.fail(403, new Throwable("user info is null"));
    }else {
      ctx.response().end(authInfo.toString());
    }
  }

  @Override
  public void handle(RoutingContext ctx) {
    router.handleContext(ctx);
  }

}

class DummyAuthProvider implements AuthorizationProvider {

  @Override
  public String getId() {
    return null;
  }

  @Override
  public void getAuthorizations(User user, Handler<AsyncResult<Void>> handler) {
    System.out.println(user.toString());
  }
}

class DummyAuthorization implements Authorization {

  private String storedUsername = "foobar";
  private String storedPassword = "pwd";
  private String role = "role:admin";

  @Override
  public boolean match(AuthorizationContext context) {
    return false;
  }

  @Override
  public boolean verify(Authorization authorization) {
    return false;
  }
}

class DummyAuthHandler extends AuthorizationHandlerImpl {
  public DummyAuthHandler(Authorization authProvider) {
    super(authProvider);
  }

}

