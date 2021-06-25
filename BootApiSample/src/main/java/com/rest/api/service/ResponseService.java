package com.rest.api.service;

import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.MultiResult;
import com.rest.api.model.response.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    public enum CommonResponse {
        SUCCESS(0, "성공"),
        FAIL(-1, "실패")
        ;

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() { return code; }
        public String getMsg() { return msg; }
    }

    // 단일 결과 처리 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 다중 결과 처리 메소드
    public <T> MultiResult<T> getMultiResult(List<T> list) {
        MultiResult<T> result = new MultiResult<>();
        result.setData(list);
        setSuccessResult(result);
        return result;
    }

    // api 요청 성공 데이터 세팅
    private void setSuccessResult(CommonResult result) {
        result.setResult(Boolean.TRUE);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 성공 결과 처리
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    // 실패 결과 처리
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        result.setResult(Boolean.FALSE);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
        return result;
    }

    // i18n 적용한 실패 결과 처리
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setResult(Boolean.FALSE);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
