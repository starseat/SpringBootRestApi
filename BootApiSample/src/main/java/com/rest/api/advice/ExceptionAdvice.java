package com.rest.api.advice;

import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.model.response.CommonResult;
import com.rest.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice  // 예외 발생시 json 형태로 결과 반환
//@RestControllerAdvice(basePackages = "com.rest.api")  // 해당 패키지 하위만 적용
public class ExceptionAdvice {
    private final ResponseService responseService;

//    @ExceptionHandler(Exception.class)  // Exception 발생히 해당 Handler 로 처리하겠다고 명시
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Exception 발생히 Http Error 500 으로 설정
//    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
//        return responseService.getFailResult();
//    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
        return responseService.getFailResult();
    }
}
