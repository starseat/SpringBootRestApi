package com.rest.api.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    @ApiModelProperty(value = "응답 성공 결과 : true/false")
    private boolean result;

    @ApiModelProperty(value = "응답 코드 번호 : {code} >= 0 - 정상, {code} < 0 - 비정상")
    private int code;

    @ApiModelProperty(value = "응답 메시지")
    private String msg;
}
