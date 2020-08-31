package com.example.handler;

import com.example.dao.Role;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.HashingStrategy;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jdbc.*;
import io.vertx.ext.auth.jdbc.impl.JDBCUserUtilImpl;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.impl.HttpStatusException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public interface JdbcHandler extends Handler<RoutingContext> {
  static JdbcHandler create(Vertx vertx, Router router) {
    return new JdbcHandlerImpl(vertx, router);
  }
}

class JdbcHandlerImpl implements JdbcHandler {

  private static final String INSERT_USER = "INSERT INTO user (username, password, password_salt) VALUES (?, ?, ?)";
  private static final String INSERT_USER_ROLE = "INSERT INTO user_roles (username, role) VALUES (?, ?)";
  private static final String INSERT_ROLE_PERMISSION = "INSERT INTO roles_perms (role, perm) VALUES (?, ?)";
  private final static String DEFAULT_ROLES_QUERY = "select role from user_roles where username = ?";
  private final static String DEFAULT_PERMISSIONS_QUERY = "select perm from roles_perms rp, user_roles ur where ur.username = ? and ur.role = rp.role";
  private static final String SELECT_USER = "select password_salt from user where username = ?";

  private final HashingStrategy strategy = HashingStrategy.load();
  private final SecureRandom random = new SecureRandom();

  private final Router router;
  private final JDBCAuthentication jdbcAuthentication;
  private final JDBCAuthorization jdbcAuthorization;

  JDBCClient client;

  public JdbcHandlerImpl(Vertx vertx, Router superRouter) {

    JsonObject config = new JsonObject();
    config.put("url", "jdbc:mysql://192.168.0.247:32495/testdb?autoReconnect=true&useSSL=false");
    config.put("driver_class", "com.mysql.cj.jdbc.Driver");
    config.put("user", "root");
    config.put("password", "dataSharing");

    // Subsequent calls will return a new client instance that uses the same data source, **so the configuration won’t be used**.
    client = JDBCClient.createShared(vertx, config, "DS-1");
    jdbcAuthentication = JDBCAuthentication.create(client, new JDBCAuthenticationOptions().setAuthenticationQuery(SELECT_USER));
    jdbcAuthorization = JDBCAuthorization.create("admin", client, new JDBCAuthorizationOptions().setRolesQuery(DEFAULT_ROLES_QUERY).setPermissionsQuery(DEFAULT_PERMISSIONS_QUERY));

    this.router = Router.router(vertx);
    router.get("/login").handler(this::login);
    router.get("/createUser").handler(this::createUser);
    router.get("/createRole").handler(this::createRole);
    router.get("/createPerm").handler(this::createPerm);
    router.get("/selectUser").handler(this::selectUser);
    router.mountSubRouter("/jdbc", superRouter);
  }

  void selectUser(RoutingContext ctx) {
    client.getConnection(res->{
      if (res.succeeded()) {
        SQLConnection conn = res.result();
        conn.query("SELECT * FROM user_roles", _res -> {
          if (_res.succeeded()) {
            ResultSet rs = _res.result();
            List<JsonObject> rows = rs.getRows();
            // Do something with results
//            for(JsonObject row : rows){
//              Role role = row.mapTo(Role.class);
//              System.out.printf("username: %s,role: %s\n", role.username, role.role);
//            }
//            ctx.response().end(rows.toString());
            List<Role> roles = rows.stream().map(row->row.mapTo(Role.class)).collect(Collectors.toList());
            ctx.response().end(Json.encodePrettily(roles));
          }
        });
      } else {
        ctx.fail(res.cause());
      }
    });
  }

  void login(RoutingContext ctx) {
    Session session = ctx.session();

    JsonObject credentials = new JsonObject();
    credentials.put("username", "foobar");
    credentials.put("password", "pwd");
    jdbcAuthentication.authenticate(credentials, res -> {
      if (res.succeeded()) {
        User user = res.result();
        if (user != null) {
          jdbcAuthorization.getAuthorizations(user, _res -> {
            if (_res.succeeded()) {
              session.put(ApiHandler.AUTH_INFO, user);
              JsonObject json = new JsonObject();
              json.put("authorizations", user.authorizations().getProviderIds().toString());
              json.put("principal", user.principal());
              ctx.response().end(json.toString());
            } else {
              ctx.fail(_res.cause());
            }
          });
        } else {
          ctx.response().end();
        }
      } else {
        ctx.fail(403, res.cause());
      }

    });
  }

  void createRole(RoutingContext ctx) {
    JDBCUserUtil util = new JDBCUserUtilImpl(client, INSERT_USER, INSERT_USER_ROLE, INSERT_ROLE_PERMISSION);
    util.createUserRole("foobar", "role:admin");
    ctx.response().end("OK");
  }

  void createPerm(RoutingContext ctx) {
    JDBCUserUtil util = new JDBCUserUtilImpl(client, INSERT_USER, INSERT_USER_ROLE, INSERT_ROLE_PERMISSION);
    util.createRolePermission("role:admin", "1,2,3,4,5");
    ctx.response().end("OK");
  }

  void createUser(RoutingContext ctx) {
    HttpServerRequest request = ctx.request();
    String username = request.getParam("username");
    String password = request.getParam("password");

    if (username == null || password == null) {
      ctx.fail(new HttpStatusException(403, "username or password are null"));
      return;
    }
    // we have all required data to insert a user
    final byte[] salt = new byte[32];
    random.nextBytes(salt);

    createHashedUser(
      username,
      password,
      strategy.hash("pbkdf2",
        null,
        Base64.getMimeEncoder().encodeToString(salt),
        password),
      ctx
    );
  }

  void createHashedUser(String username, String password, String hash, RoutingContext ctx) {

    client.updateWithParams(
      INSERT_USER,
      new JsonArray().add(username).add(password).add(hash),
      insert -> {
        if (insert.succeeded()) {
          ctx.response().end("OK");
        } else {
          ctx.fail(insert.cause());
        }
      });
  }

  @Override
  public void handle(RoutingContext ctx) {
    router.handleContext(ctx);// sub router handle
  }

}
