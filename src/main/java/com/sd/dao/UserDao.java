package com.sd.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDao {
  public String username;
  public String password;
  @JsonProperty("password_salt")
  public String passwordSalt;
}
