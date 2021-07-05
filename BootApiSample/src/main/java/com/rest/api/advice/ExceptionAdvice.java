package com.rest.api.advice;

import com.rest.api.advice.exception.AuthenticationEntryPointException;
import com.rest.api.advice.exception.EmailSigninFailedException;
import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.model.response.CommonResult;
import com.rest.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice  // 예외 발생시 json 형태로 결과 반환
//@RestControllerAdvice(basePackages = "com.rest.api")  // 해당 패키지 하위만 적용
public class ExceptionAdvice {
    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)  // Exception 발생히 해당 Handler 로 처리하겠다고 명시
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Exception 발생히 Http Error 500 으로 설정
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        //return responseService.getFailResult();

        // i18n 적용
        return responseService.getFailResult(
                Integer.valueOf(getMessage("unKnown.code")),
                getMessage("unKnown.msg")
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundException e) {
        //return responseService.getFailResult();

        // i18n 적용
        return responseService.getFailResult(
                Integer.valueOf(getMessage("userNotFound.code")),
                getMessage("userNotFound.msg")
        );
    }

    @ExceptionHandler(EmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailed(HttpServletRequest request, EmailSigninFailedException e) {
        return responseService.getFailResult(
                Integer.valueOf(getMessage("emailSigninFailed.code")),
                getMessage("emailSigninFailed.msg")
        );
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, AuthenticationEntryPointException e) {
        return responseService.getFailResult(
                Integer.valueOf(getMessage("entryPointException.code")),
                getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(
                Integer.valueOf(getMessage("accessDenied.code")),
                getMessage("accessDenied.msg")
        );
    }



    // code 정보에 해당하는 메시지 조회
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code 정보, 추가 argument로 현재 local에 맞는 메시지 조회
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
