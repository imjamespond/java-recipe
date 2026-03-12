package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.*;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;

@Component
public class EnumObjectConverter implements ModelConverter {

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        Class<?> rawClass = null;

        if (type.getType() instanceof Class<?>) {
            rawClass = (Class<?>) type.getType();
        } else if (type.getType() instanceof JavaType) {
            rawClass = ((JavaType) type.getType()).getRawClass();
        }

        if (rawClass != null && rawClass.isEnum()) {

            JsonFormat jsonFormat = rawClass.getAnnotation(JsonFormat.class);

            if (jsonFormat != null && jsonFormat.shape() == JsonFormat.Shape.OBJECT) {

                ObjectSchema schema = new ObjectSchema();
                schema.setName(rawClass.getSimpleName());

                // 通过反射读取字段
                Field[] fields = rawClass.getDeclaredFields();

                for (Field field : fields) {

                    // 过滤 enum 常量 / static / synthetic
                    if (field.isEnumConstant()) continue;
                    if (Modifier.isStatic(field.getModifiers())) continue;
                    if (field.isSynthetic()) continue;

                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();

                    Schema<?> propertySchema = convertToSchema(fieldType);

                    if (propertySchema != null) {
                        schema.addProperties(fieldName, propertySchema);
                    }
                }

                return schema;
            }
        }

        return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
    }

    /**
     * Java 类型 -> Swagger Schema
     */
    private Schema<?> convertToSchema(Class<?> type) {

        if (type == String.class) {
            return new StringSchema();
        }

        if (type == Integer.class || type == int.class) {
            return new IntegerSchema();
        }

        if (type == Long.class || type == long.class) {
            return new IntegerSchema().format("int64");
        }

        if (type == Boolean.class || type == boolean.class) {
            return new BooleanSchema();
        }

        if (type == Double.class || type == double.class) {
            return new NumberSchema().format("double");
        }

        if (type == Float.class || type == float.class) {
            return new NumberSchema().format("float");
        }

        // 默认 fallback
        return new StringSchema();
    }
}