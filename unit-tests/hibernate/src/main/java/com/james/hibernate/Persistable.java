package com.james.hibernate;

import java.io.Serializable;

public abstract interface Persistable
{
  public abstract Serializable id();
}
