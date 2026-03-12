package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.VerifyExpressionType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;


@RestController
public class FoobarController {

    @GetMapping("/foobar")
    public String foobar() {
        return "Hello Foobar";
    }

    @Operation(summary = "参数-检查属性类型列表")
    @GetMapping("/getAllVerifyExpressionTypes")
    public List<VerifyExpressionType> getAllVerifyExpressionTypes() {
        return Arrays.asList(VerifyExpressionType.values());
    }

}
