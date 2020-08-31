package com.example.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.auth.authorization.impl.RoleBasedAuthorizationImpl;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.impl.AuthorizationHandlerImpl;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import rx.Observable;
import rx.schedulers.Schedulers;

public interface ApiHandler extends Handler<RoutingContext> {
  String AUTH_INFO = "AUTH_INFO";
  String USER_NAME = "username";
  String PASSWORD = "password";

  static ApiHandler create(Vertx vertx, Router router) {
    return new ApiHandlerImpl(vertx, router);
  }
}

class ApiHandlerImpl implements ApiHandler {

  private final Router router;

  public ApiHandlerImpl(Vertx vertx, Router superRouter) {
    this.router = Router.router(vertx);

    SessionStore store = LocalSessionStore.create(vertx);
    SessionHandler sessionHandler = SessionHandler
      .create(store)
      .setNagHttps(false)// allow http
      .setSessionTimeout(60000l);// 1 minute

    router.route().handler(sessionHandler);
    router.get("/set").handler(this::setSession);

    GithubHandler.create(vertx, router);
    OAuth2Handler.create(vertx, router);
    JdbcHandler.create(vertx, router);

    router.route().handler(new AdminAuthHandler());// protected by AuthHandler
    router.get("/get").handler(this::getSession);

    superRouter.mountSubRouter("/api", router);
  }

  private void setSession(RoutingContext ctx) {
    HttpServerRequest request = ctx.request();
    Session session = ctx.session();

    JsonObject authInfo = new JsonObject();
    authInfo.put(USER_NAME, request.getParam(USER_NAME));//dummy checking
    authInfo.put(PASSWORD, request.getParam(PASSWORD));
    session.put(AUTH_INFO, authInfo);

    ctx.response().end("OK");
  }

  private void getSession(RoutingContext ctx) {
    Session session = ctx.session();
    User authInfo = session.get(AUTH_INFO);
    if (authInfo == null) {
      ctx.fail(403, new Throwable("user info is null"));
    } else {
      ctx.response().end(authInfo.principal().toString());
    }
  }

  @Override
  public void handle(RoutingContext ctx) {
    router.handleContext(ctx);
  }


  class DummyAuthProvider implements AuthorizationProvider {

    @Override
    public String getId() {
      return "dummy";
    }

    @Override
    public void getAuthorizations(User user, Handler<AsyncResult<Void>> handler) {
      // called in checkOrFetchAuthorizations of AuthorizationHandlerImpl recursively with each provider
      // fetch authorization from this provider
      Observable
        .just("dummy")
        .doOnNext(val->{
          //user.authorizations().add(val, new RoleBasedAuthorizationImpl("role:admin"));//get the role of current user asynchronously
          handler.handle(Future.succeededFuture());
        })
        .subscribeOn(Schedulers.newThread())
        .subscribe();
    }
  }

  class AdminAuthHandler extends AuthorizationHandlerImpl {
    public AdminAuthHandler() {
      super(new RoleBasedAuthorizationImpl("role:admin"));//the role needed to access
      this.addAuthorizationProvider(new DummyAuthProvider());
    }

    @Override
    public void handle(RoutingContext ctx) {

      Session session = ctx.session();
      User authInfo = session.get(AUTH_INFO);

      if (session != null) {
        if (authInfo == null) {
          ctx.fail(new HttpStatusException(401, "AuthInfo is null!"));
        } else {
          ctx.setUser(authInfo);
          super.handle(ctx);
        }
      }else {
        ctx.fail(new HttpStatusException(401, "Invalid session context!"));
      }
    }
  }

}

