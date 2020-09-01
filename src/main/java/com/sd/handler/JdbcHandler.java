package com.sd.handler;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sd.dao.UserDao;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.HashingStrategy;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
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

import static com.sd.handler.ApiHandler.*;

public interface JdbcHandler {
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
    router.post("/login").handler(this::login);
    router.get("/createUser").handler(this::createUser);
    router.get("/createRole").handler(this::createRole);
    router.get("/createPerm").handler(this::createPerm);
    router.get("/selectUser").handler(this::selectUser);
    superRouter.mountSubRouter("/jdbc", router);
  }

  void selectUser(RoutingContext ctx) {
    client.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection conn = res.result();
        conn.query("SELECT * FROM user", _res -> {
          if (_res.succeeded()) {
            ResultSet rs = _res.result();
            List<JsonObject> rows = rs.getRows();

            List<UserDao> roles = rows.stream().map(row -> row.mapTo(UserDao.class)).collect(Collectors.toList());
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

    ctx.request().bodyHandler(bodyHandler -> {
      final JsonObject credentials = bodyHandler.toJsonObject();
      jdbcAuthentication.authenticate(credentials, res -> {
        if (res.succeeded()) {
          User user = res.result();
          if (user != null) {
            jdbcAuthorization.getAuthorizations(user, _res -> {
              if (_res.succeeded()) {
                session.put(USER_INFO, user);
                ctx.response().end();
              } else {
                ctx.fail(_res.cause());
              }
            });
          } else {
            ctx.fail(new HttpStatusException(403, "user is null"));
          }
        } else {
          ctx.fail(403, res.cause());
        }
      });
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

}


/*
--
-- Take this script with a grain of salt and adapt it to your RDBMS
--
CREATE TABLE `user` (
 `username` VARCHAR(255) NOT NULL,
 `password` VARCHAR(255) NOT NULL,
 `password_salt` VARCHAR(255) NOT NULL
);

CREATE TABLE `user_roles` (
 `username` VARCHAR(255) NOT NULL,
 `role` VARCHAR(255) NOT NULL
);

CREATE TABLE `roles_perms` (
 `role` VARCHAR(255) NOT NULL,
 `perm` VARCHAR(255) NOT NULL
);

ALTER TABLE user ADD CONSTRAINT `pk_username` PRIMARY KEY (username);
ALTER TABLE user_roles ADD CONSTRAINT `pk_user_roles` PRIMARY KEY (username, role);
ALTER TABLE roles_perms ADD CONSTRAINT `pk_roles_perms` PRIMARY KEY (role);

ALTER TABLE user_roles ADD CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES user(username);
ALTER TABLE user_roles ADD CONSTRAINT fk_roles FOREIGN KEY (role) REFERENCES roles_perms(role);
 */
