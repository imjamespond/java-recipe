package com.example.demo;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.GetMapping;


import java.util.Arrays;
import java.util.List;
import java.util.Map;



@RestController
public class FoobarController {

    @GetMapping("/foobar")
    @ApiResponse(
        responseCode = "200",
        content = @Content(
            schema = @Schema(types = {"string", "null"})
        )
    )
    public String foobar(Foobar foobar) {
        return "Hello Foobar";
    }

    @Operation(summary = "参数-检查属性类型列表")
    @GetMapping("/getAllVerifyExpressionTypes")
    public List<VerifyExpressionType> getAllVerifyExpressionTypes() {
        return Arrays.asList(VerifyExpressionType.values());
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "上传文件",
            description = "通过此接口上传单个文件",
            responses = {
                    @ApiResponse(responseCode = "200", description = "上传成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数错误")
            }
    )
    public ResponseEntity<String> uploadFile(
            @Parameter(
                    description = "要上传的文件",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart("file") MultipartFile file) {

        // 这里不实现具体功能，只打印文件名作为演示
        System.out.println("接收到文件: " + file.getOriginalFilename());
        
        return ResponseEntity.ok("文件上传接口调用成功，文件名: " + file.getOriginalFilename());
    }
}
