package com.sd.handler;

import com.sd.util.MessageDigestUtil;
import com.sd.util.RequestWrapper;
import io.vertx.core.Vertx;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.impl.RoleBasedAuthorizationImpl;
import io.vertx.ext.auth.impl.UserConverter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.impl.AuthorizationHandlerImpl;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

public interface ApiHandler {
  String USER_INFO = "USER_INFO";
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
      .setSessionTimeout(600000l);// 10 minute session

    router.route().handler(sessionHandler);
    router.get("/set").handler(this::setSession);

    JdbcHandler.create(vertx, router);

    // router.route().handler(new AdminAuthHandler());// protected by AuthHandler
    router.get("/userInfo").handler(this::getSession);
    router.post("/logout").handler(this::cleanSession);

    superRouter.mountSubRouter("/api", router);
  }

  private void cleanSession(RoutingContext ctx) {
    Session session = ctx.session();
    session.remove(USER_INFO);
    Cookie cookie = ctx.removeCookie("rememberMe", true);
    if (null != cookie) {
      cookie.setPath("/");
    }

    ctx.response().end();
  }

  private void setSession(RoutingContext ctx) {
    _setSession(ctx);
    setRememberMe(ctx);
    ctx.response().end("OK");
  }

  private void _setSession(RoutingContext ctx) {
    HttpServerRequest request = ctx.request();
    Session session = ctx.session();

    JsonObject authInfo = new JsonObject();
    RequestWrapper reqWrapper = new RequestWrapper(request);
    authInfo.put(USER_NAME, reqWrapper.getWithDefault(USER_NAME, "foobar"));//dummy checking
    authInfo.put(PASSWORD, reqWrapper.getWithDefault(PASSWORD, "pwd"));
    User user = User.create(authInfo);
    user.authorizations().add("dummy", new RoleBasedAuthorizationImpl("role:admin"));
    session.put(USER_INFO, user);

  }

  final String secret = "uWillNeverGuess";

  private void setRememberMe(RoutingContext ctx) {
    HttpServerRequest request = ctx.request();
    String rememberMe = request.getParam("rememberMe");
    if (rememberMe != null && rememberMe.compareTo("true") == 0) {

      String expired = Long.toString(System.currentTimeMillis() + 360 * 1000l);
      String sha1 = MessageDigestUtil.Sha1(secret + expired);

      Cookie rmbMeCookie = Cookie.cookie("rememberMe", String.join("$", sha1, expired)).setPath("/");
      ctx.addCookie(rmbMeCookie);
    }
  }

  private boolean checkRememberMe(RoutingContext ctx) {
    Cookie rmbMeCookie = ctx.getCookie("rememberMe");
    if (null != rmbMeCookie) {
      String rememberMe = rmbMeCookie.getValue();
      if (null != rememberMe) {
        String[] arr = rememberMe.split("\\$");
        if (arr.length == 2) {
          String sha1 = arr[0];
          String expired = arr[1];
          Long expiredVal = Long.parseLong(expired);
          if (null != expiredVal && null != sha1 && sha1.compareTo(MessageDigestUtil.Sha1(secret + expired)) == 0) {
            if (expiredVal.compareTo(System.currentTimeMillis()) > 0) {
              _setSession(ctx); //Do remember me login
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private void getSession(RoutingContext ctx) {
    Session session = ctx.session();
    User user = session.get(USER_INFO);
    if (user == null) {
      ctx.fail(403, new Throwable("user info is null"));
    } else {
      ctx.response().end(UserConverter.encode(user).toString());
    }
  }


  class AdminAuthHandler extends AuthorizationHandlerImpl {
    public AdminAuthHandler() {
      super(new RoleBasedAuthorizationImpl("role:admin"));//the role needed to access
    }

    @Override
    public void handle(RoutingContext ctx) {
      Session session = ctx.session();
      if (session != null) {
        User authInfo = session.get(USER_INFO);
        if (authInfo == null) {
          if (checkRememberMe(ctx)) {
            authInfo = session.get(USER_INFO);
            ctx.setUser(authInfo);
            super.handle(ctx);
          } else {
            ctx.fail(new HttpStatusException(401, "user info is null"));
          }
        } else {
          ctx.setUser(authInfo);
          super.handle(ctx);
        }
      } else {
        ctx.fail(new HttpStatusException(401, "Invalid session context!"));
      }
    }
  }

}
