package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperationItem {

  EDIT("edit", "edit", "编辑"),
  DELETE("delete", "delete", "删除");

  private final String id;
  private final String name;
  private final String cnName;

  OperationItem(String id, String name, String cnName) {
    this.id = id;
    this.name = name;
    this.cnName = cnName;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCnName() {
    return cnName;
  }
}