package com.rest.api.controller.v1;

import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.MultiResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// SecurityConfiguration 에서 설정한것 적용 @PreAuthorize 또는 @Secured 사용
// controller 전체 적용 가능
//@PreAuthorize("hasRole('ROLE_USER')")
//@Secured("ROLE_USER")

@Api(tags = {"2. User"})  // swagger 설정
@RequiredArgsConstructor  // 이거 대신 선언된 객체에 @Autowired 사용해도 됨.
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;  // 결과 처리 response

//    @Secured("ROLE_USER") // 메소드에 개별 설정 가능
    @ApiImplicitParams({
            // 유효한 JWT 토큰 설정해야만 리소스 사용할 수 있도록 Header에 X-AUTH-TOKEN 인자 받음.
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping(value = "/users")
    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회한다.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
//            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
//    })
//    public List<User> findAllUser() { return userJpaRepo.findAll(); }
    public MultiResult<User> findAllUser(
            @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
    ) {
        return responseService.getMultiResult(userJpaRepo.findAll());
    }

////    @PreAuthorize("hasRole('ROLE_USER')") // 메소드에 개별 설정 가능
//    @ApiImplicitParams({
//            // 유효한 JWT 토큰 설정해야만 리소스 사용할 수 있도록 Header에 X-AUTH-TOKEN 인자 받음.
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @GetMapping(value = "/user/{msrl}")
//    @ApiOperation(value = "회원 조회", notes = "User ID로 회원을 조회한다.")
////    @ApiResponses(value = {
////            @ApiResponse(code = 200, message = "Success"),
////            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
////            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
////            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
////    })
//    public SingleResult<User> findUserById(
//            @ApiParam(value = "회원ID", required = true) @PathVariable long msrl,
//            @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
//    ) {
//        //return responseService.getSingleResult(userJpaRepo.findById(msrl).orElse(null));
//        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(UserNotFoundException::new));
//    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<User> findUser() {
        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        return responseService.getSingleResult(userJpaRepo.findByUid(id).orElseThrow(UserNotFoundException::new));
    }

    // 유저 등록은 SignController 의 회원가입으로 변경
//    @ApiImplicitParams({
//            // 유효한 JWT 토큰 설정해야만 리소스 사용할 수 있도록 Header에 X-AUTH-TOKEN 인자 받음.
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @PostMapping(value = "/user")
//    @ApiOperation(value = "회원 등록", notes = "회원을 등록한다.")
////    @ApiResponses(value = {
////            @ApiResponse(code = 200, message = "Success"),
////            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
////            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
////            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
////    })
//    public SingleResult<User> save(
//            @ApiParam(value = "회원 ID", required = true)  @RequestParam String uid,
//            @ApiParam(value = "회원 이름", required = true) @RequestParam String name
////            , @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
//    ) {
//        User user = User.builder()
//                .uid(uid)
//                .name(name)
//                .build();
//        return responseService.getSingleResult(userJpaRepo.save(user));
//    }

    @ApiImplicitParams({
            // 유효한 JWT 토큰 설정해야만 리소스 사용할 수 있도록 Header에 X-AUTH-TOKEN 인자 받음.
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping(value = "/user")
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
//            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
//    })
    public SingleResult<User> modify(
            @ApiParam(
                    value = "회원 번호", required = true,
                    name = "User Number", type = "long", example = "0"
            ) @RequestParam long msrl,
            @ApiParam(value = "회원 ID", required = true) @RequestParam String uid,
            @ApiParam(value = "회원 이름", required = true) @RequestParam String name
//            , @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
    ) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @ApiImplicitParams({
            // 유효한 JWT 토큰 설정해야만 리소스 사용할 수 있도록 Header에 X-AUTH-TOKEN 인자 받음.
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping(value = "/user/{msrl}")
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
//            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
//    })
    public CommonResult delete(
            @ApiParam(
                    value = "회원 번호", required = true,
                    name = "User Number", type = "long", example = "0"
            )
            @PathVariable long msrl
//            , @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
    ) {
        userJpaRepo.deleteById(msrl);
        return responseService.getSuccessResult();
    }
}
