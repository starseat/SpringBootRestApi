package com.rest.api.controller.v1;

import com.rest.api.advice.exception.EmailSigninFailedException;
import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import com.rest.config.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 회원 가입 및 로그인 처리
 *  - 로그인 성공시 결과로 JWT 토큰 발급
 *  - 가입시 패스워드 인코딩을 위해 passwordEncoder 설정. 기본 설정은 bcrypt encoding 사용 됨.
 */
@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password
    ) {
        User user = userJpaRepo.findByUid(id).orElseThrow(EmailSigninFailedException::new);
        if ( !passwordEncoder.matches(password, user.getPassword()) ) {
            throw new EmailSigninFailedException();
        }

        return responseService.getSingleResult(
                jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles())
        );
    }

    @ApiOperation(value = "가입", notes = "회원 가입")
    @PostMapping(value = "/signup")
    public CommonResult signup(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "이름", required = true) @RequestParam String name
    ) {
        userJpaRepo.save(
                User.builder()
                    .uid(id)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build()
        );

        return responseService.getSuccessResult();
    }
}
