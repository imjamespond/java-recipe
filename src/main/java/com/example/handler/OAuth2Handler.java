package com.example.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.impl.UserImpl;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public interface OAuth2Handler extends Handler<RoutingContext> {
  static OAuth2Handler create(Vertx vertx, Router router) {
    return new OAuth2HandlerImpl(vertx, router);
  }
}

class OAuth2HandlerImpl implements OAuth2Handler {
  final Logger log = LoggerFactory.getLogger(OAuth2HandlerImpl.class);
  final String OAUTH2_TOKEN = "OAUTH2_TOKEN";
  final String REQUEST_URI = "REQUEST_URI";

  private final Router router;

  OAuth2Auth oauth2;
  WebClient client;

  public OAuth2HandlerImpl(Vertx vertx, Router superRouter) {
    this.router = Router.router(vertx);

    router.get("/callback").handler(this::callback);
    router.route("/*").handler(this::test);
    router.get("/userInfo").handler(this::userInfo);
    router.get("/refresh").handler(this::refresh);
    router.get("/date").handler(this::date);

    router.mountSubRouter("/oauth2", superRouter);

    // Initialize the OAuth2 Library
    oauth2 = OAuth2Auth.create(vertx, new OAuth2Options()
      .setFlow(OAuth2FlowType.AUTH_CODE)
      .setClientID("second-client")
      .setClientSecret("noonewilleverguess")
      .setSite("http://localhost:8081/uac")
      .setTokenPath("/oauth/token")
      .setAuthorizationPath("/oauth/authorize")
      .setUserInfoPath("http://localhost:8085/whoami")
      .setScopeSeparator(" ")
      .setHeaders(new JsonObject()
        .put("User-Agent", "vertx-auth-oauth2")));
    client = WebClient.create(vertx,
      new WebClientOptions().setUserAgent("My-App/1.2.3").setKeepAlive(false));
  }

  private void date(RoutingContext ctx) {
    Session session = ctx.session();
    OAuth2Token token = session.get(OAUTH2_TOKEN);

    // Send a GET request
    client
      .getAbs("http://localhost:8085/test")
      .putHeader("Authorization", String.format("Bearer %s", token.accessToken))
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          ctx.response()
            .setStatusCode(response.statusCode())
            .end(response.bodyAsString());
        } else {
          ctx.fail(new Throwable(ar.cause().getMessage()));
        }
      });
  }

  private void refresh(RoutingContext ctx) {
    Session session = ctx.session();
    OAuth2Token github = session.get(OAUTH2_TOKEN);

    JsonObject rs = new JsonObject();

    oauth2.refresh(github.user, res->{
      if (res.succeeded()){
        User token = res.result();
        String accessToken = token.principal().getString("access_token");
        String refreshToken = token.principal().getString("refresh_token");

        session.put(OAUTH2_TOKEN, new OAuth2Token(accessToken, refreshToken, token));
        ctx.response().end( rs
          .put("before", Arrays.asList(github.accessToken,github.refreshToken))
          .put("now", Arrays.asList(accessToken,refreshToken)).toString() );
      } else {
        ctx.fail(403, res.cause());
      }
    });
  }

  private void userInfo(RoutingContext ctx) {
    Session session = ctx.session();
    OAuth2Token github = session.get(OAUTH2_TOKEN);

    // Send a GET request
    oauth2.userInfo(github.user, res->{
      if (res.succeeded()){
        ctx.response().end(res.result().toString());
      }
    });
  }

  private void callback(RoutingContext ctx) {
    HttpServerRequest request = ctx.request();

    // in this case GitHub will call you back in the callback uri one should now complete the handshake as:

    // the code is provided as a url parameter by github callback call
    String code = request.getParam("code");
    String state = request.getParam("state");
    log.info("callback code:{}, state:{}", code, state);

    if (code==null) {
      ctx.fail(403, new Throwable("code is null"));
      return;
    }

    JsonObject tokenConfig = new JsonObject()
      .put("code", code)
      .put("redirect_uri", "http://localhost:8888/api/oauth2/callback");// strict redirect_uri match required by auth server

    // Callbacks
    // Save the access token
    oauth2.authenticate(tokenConfig, res -> {
      if (res.failed()) {
        log.error("Access Token Error: {}", res.cause().getMessage());
        ctx.fail(403, res.cause());
      } else {
        // Get the access token object (the authorization code is given from the previous step).
        User token = (UserImpl)res.result();
        log.info(token.principal().toString());

        String accessToken = token.principal().getString("access_token");
        String refreshToken = token.principal().getString("refresh_token");
        Session session = ctx.session();
        session.put(OAUTH2_TOKEN, new OAuth2Token(accessToken,refreshToken, token));

        String uri = session.get(REQUEST_URI);
        if (uri != null) {
          ctx.response().putHeader("Location", uri).setStatusCode(302).end();
        } else {
          ctx.response().end();
        }

      }
    });
  }

  private void test(RoutingContext ctx) {
    Session session = ctx.session();
    OAuth2Token github = session.get(OAUTH2_TOKEN);
    if (github != null) {
      ctx.next();
      return;
    }

    HttpServerRequest request = ctx.request();
    HttpServerResponse response = ctx.response();

    session.put(REQUEST_URI, request.uri());

    // when there is a need to access a protected resource or call a protected method,
    // call the authZ url for a challenge

    // Authorization oauth2 URI
    String authorization_uri = oauth2.authorizeURL(new JsonObject()
      .put("redirect_uri", "http://localhost:8888/api/oauth2/callback")// and return a code for exchanging an access token
      .put("scope", "resource:read")
      .put("state", "3(#0/!~"));

    // when working with web application use the above string as a redirect url

    // Redirect to **Github** using Vert.x
    response
      .putHeader("Location", authorization_uri)
      .setStatusCode(302)
      .end();
  }


  @Override
  public void handle(RoutingContext ctx) {
    router.handleContext(ctx);
  }


  class OAuth2Token {
    public String accessToken;
    public String refreshToken;
    public User user;

    public OAuth2Token(String accessToken, String refreshToken, User token) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
      this.user = token;
    }
  }
}


