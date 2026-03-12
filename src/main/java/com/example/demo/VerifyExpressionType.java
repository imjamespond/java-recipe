package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
// @Schema(description = "校验表达式类型对象",implementation = VerifyExpressionType.VO.class)
public enum VerifyExpressionType {

    STRING_CONTENT("stringContent", "文本（内容）"),
    STRING_LENGTH("stringLength", "数值（长度）"),
    STRING_ENUM("stringEnum", "枚举"),
    NORMAL("normal", "通用");

    public static final String[] ARRAYS = stream(values()).map(VerifyExpressionType::getExpressionTypeEnName).toArray(String[]::new);

    public static final Map<String, String> MAP = stream(VerifyExpressionType.values())
            .collect(Collectors.toMap(
                    VerifyExpressionType::getExpressionTypeEnName,
                    VerifyExpressionType::getExpressionTypeCnName,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
            ));

    // @Schema(description = "英文标识")
    private final String expressionTypeEnName;
    // @Schema(description = "中文名称")
    private final String expressionTypeCnName;

    VerifyExpressionType(String expressionTypeEnName, String expressionTypeCnName) {
        this.expressionTypeEnName = expressionTypeEnName;
        this.expressionTypeCnName = expressionTypeCnName;
    }

    public String getExpressionTypeCnName() {
        return expressionTypeCnName;
    }

    public String getExpressionTypeEnName() {
        return expressionTypeEnName;
    }


    // @Schema(name = "DataModelState", description = "DataModel状态对象")
    // public static class VO {
    //     @Schema(description = "状态ID", allowableValues = {"1", "2", "4-0", "4-1", "4-2", "all"})
    //     public String id;

    //     @Schema(description = "状态名称", allowableValues = {"草稿", "评审中", "评审通过", "已上线", "已下线", "所有模型, 不同状态算一个"})
    //     public String name;
    // }
}
