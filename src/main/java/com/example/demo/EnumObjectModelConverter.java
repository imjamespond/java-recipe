package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JavaType;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.*;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import org.springframework.stereotype.Component;

// @Component
public class EnumObjectModelConverter implements ModelConverter {

  @Override
  public Schema resolve(
      AnnotatedType type,
      ModelConverterContext context,
      Iterator<ModelConverter> chain) {

    System.out.println("进入自定义 Converter: " + type.getType());

    if (type.getType() instanceof JavaType javaType) {
      Class<?> clazz = javaType.getRawClass();

      System.out.println("类型：" + clazz);
      System.out.println("是否Enum：" + clazz.isEnum());

      JsonFormat format = clazz.getAnnotation(JsonFormat.class);

      System.out.println("JsonFormat：" + format);

      if (format != null) {
        System.out.println("Shape：" + format.shape());
      }

      if (clazz != null
          && clazz.isEnum()
          && isObjectEnum(clazz)) {

        System.out.println("命中对象枚举：" + clazz);

        return buildEnumSchema(clazz);
      }
    }

    Type javaType = type.getType();

    if (!(javaType instanceof Class<?> clazz)
        || !clazz.isEnum()
        || !isObjectEnum(clazz)) {

      return chain.hasNext()
          ? chain.next().resolve(type, context, chain)
          : null;
    }

    return buildEnumSchema(clazz);
  }

  private boolean isObjectEnum(Class<?> clazz) {
    JsonFormat format = clazz.getAnnotation(JsonFormat.class);

    System.out.println("JsonFormat = " + format);

    if (format == null) {
      return false;
    }

    System.out.println("Shape = " + format.shape());

    return format.shape() == JsonFormat.Shape.OBJECT;
  }

  private Schema buildEnumSchema(Class<?> clazz) {

    ComposedSchema result = new ComposedSchema();
    List<Schema> variants = new ArrayList<>();

    Method[] getters = Arrays.stream(clazz.getMethods())
        .filter(m -> m.getParameterCount() == 0)
        .filter(m -> !m.getName().equals("getDeclaringClass"))
        .filter(m -> !m.getName().equals("getClass"))
        .filter(m -> m.getName().startsWith("get")
            || m.getName().startsWith("is"))
        .toArray(Method[]::new);

    for (Object constant : clazz.getEnumConstants()) {

      ObjectSchema object = new ObjectSchema();
      List<String> required = new ArrayList<>();

      for (Method getter : getters) {
        try {
          String propertyName = getPropertyName(getter);
          Object value = getter.invoke(constant);

          Schema propertySchema = createLiteralSchema(value);

          object.addProperty(propertyName, propertySchema);
          required.add(propertyName);

        } catch (Exception e) {
          throw new IllegalStateException(
              "Failed to inspect enum getter: " + getter, e);
        }
      }

      object.setRequired(required);
      variants.add(object);
    }

    result.setOneOf(variants);
    result.setDescription(clazz.getSimpleName());

    return result;
  }

  private String getPropertyName(Method method) {

    String name = method.getName();

    if (name.startsWith("get")) {
      name = name.substring(3);
    } else if (name.startsWith("is")) {
      name = name.substring(2);
    }

    return Character.toLowerCase(name.charAt(0))
        + name.substring(1);
  }

  private Schema createLiteralSchema(Object value) {

    if (value == null) {
      return new Schema<>().nullable(true);
    }

    Schema schema;

    if (value instanceof String) {
      schema = new StringSchema();
    } else if (value instanceof Integer || value instanceof Short
        || value instanceof Byte) {
      schema = new IntegerSchema();
    } else if (value instanceof Long) {
      schema = new IntegerSchema();
      schema.setFormat("int64");
    } else if (value instanceof Number) {
      schema = new NumberSchema();
    } else if (value instanceof Boolean) {
      schema = new BooleanSchema();
    } else {
      schema = new StringSchema();
    }

    schema.setEnum(Collections.singletonList(value));

    return schema;
  }

  private Method[] getEnumGetters(Class<?> clazz) {

    return Arrays.stream(clazz.getDeclaredMethods())
        .filter(m -> !m.isSynthetic())
        .filter(m -> !m.isBridge())
        .filter(m -> !java.lang.reflect.Modifier.isStatic(
            m.getModifiers()))
        .filter(m -> m.getParameterCount() == 0)
        .filter(m -> m.getReturnType() != void.class)
        .filter(m -> m.getName().startsWith("get")
            || m.getName().startsWith("is"))
        .filter(m -> !m.getName().equals("getDeclaringClass"))
        .sorted(Comparator.comparing(Method::getName))
        .toArray(Method[]::new);
  }

}
