package com.example.demo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

public class Foobar {

    // @Schema(description = "Foo 对象，可以为 null", nullable = true) OPENAPI_30
    @Schema(description = "Foo",  types = { "object",  "null" } )
    public Foo foo;

    public static class Foo {
        @Schema(description = "value", types = {"string", "null"})
        public String value; 
    }

    public static class Nullable {
    }
}