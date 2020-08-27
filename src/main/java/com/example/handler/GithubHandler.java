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
import io.vertx.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface GithubHandler extends Handler<RoutingContext> {
  static GithubHandler create(Vertx vertx, Router router) {
    return new GithubHandlerImpl(vertx, router);
  }
}

class GithubHandlerImpl implements GithubHandler {
  final Logger log = LoggerFactory.getLogger(GithubHandlerImpl.class);
  final String GITHUB_TOKEN = "GITHUB_TOKEN";

  private final Router router;

  OAuth2Auth oauth2;
  WebClient client;

  public GithubHandlerImpl(Vertx vertx, Router superRouter) {
    this.router = Router.router(vertx);

    router.get("/callback").handler(this::callback);
    router.route("/*").handler(this::test);
    router.get("/userInfo").handler(this::userInfo);
    router.get("/repos").handler(this::repos);

    router.mountSubRouter("/github", superRouter);

    // Initialize the OAuth2 Library
    oauth2 = GithubAuth.create(vertx, "61a625266449af34018a", "67e47ae896b28fd58be88a03aa0eeb87cbe823f3");
    client = WebClient.create(vertx,
      new WebClientOptions().setUserAgent("My-App/1.2.3").setKeepAlive(false));
  }


  private void userInfo(RoutingContext ctx) {
    Session session = ctx.session();
    Github github = session.get(GITHUB_TOKEN);

    // Send a GET request
    oauth2.userInfo(github.user, res->{
      if (res.succeeded()){
        ctx.response().end(res.result().toString());
      } else {
        ctx.fail(403, res.cause());
      }
    });
  }

  private void repos(RoutingContext ctx) {
    Session session = ctx.session();
    Github github = session.get(GITHUB_TOKEN);

    // Send a GET request
    client
      .getAbs("https://api.github.com:443/user/repos")
      .putHeader("Accept", "application/vnd.github.v3+json")
      .putHeader("Authorization", String.format("token %s", github.accessToken))
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
      .put("redirect_uri", "http://localhost");

    // Callbacks
    // Save the access token
    oauth2.authenticate(tokenConfig, res -> {
      if (res.failed()) {
        log.error("Access Token Error: {}", res.cause().getMessage());
        ctx.fail(403, res.cause());
      } else {
        // Get the access token object (the authorization code is given from the previous step).
        UserImpl token = (UserImpl)res.result();
        log.info(token.principal().toString());

        String accessToken = token.principal().getString("access_token");
        Session session = ctx.session();
        session.put(GITHUB_TOKEN, new Github(accessToken, token));

        ctx.response().end();
      }
    });
  }

  private void test(RoutingContext ctx) {
    Session session = ctx.session();
    Github github = session.get(GITHUB_TOKEN);
    if (github != null) {
      ctx.next();
      return;
    }

    HttpServerResponse response = ctx.response();

    // when there is a need to access a protected resource or call a protected method,
    // call the authZ url for a challenge

    // Authorization oauth2 URI
    String authorization_uri = oauth2.authorizeURL(new JsonObject()
      .put("redirect_uri", "http://localhost:8888/api/github/callback")// and return a code for exchanging an access token
      .put("scope", "notifications")
      .put("state", "3(#0/!~"));

    // when working with web application use the above string as a redirect url

    // Redirect to **Github** using Vert.x
    response.putHeader("Location", authorization_uri)
      .setStatusCode(302)
      .end();
  }


  @Override
  public void handle(RoutingContext ctx) {
    router.handleContext(ctx);
  }


  class Github {
    public String accessToken;
    public String refreshToken;
    public User user;

    public Github(String accessToken, User token) {
      this.accessToken = accessToken;
      this.user = token;
    }
  }
}


