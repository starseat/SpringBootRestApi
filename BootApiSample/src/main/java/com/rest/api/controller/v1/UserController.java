package com.rest.api.controller.v1;

import com.rest.api.entity.User;
import com.rest.api.model.response.CommonResult;
import com.rest.api.model.response.MultiResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.UserJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"User"})  // swagger 설정
@RequiredArgsConstructor  // 이거 대신 선언된 객체에 @Autowired 사용해도 됨.
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;  // 결과 처리 response

    @GetMapping(value = "/users")
    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
    })
//    public List<User> findAllUser() { return userJpaRepo.findAll(); }
    public MultiResult<User> findAllUser() {
        return responseService.getMultiResult(userJpaRepo.findAll());
    }

    @GetMapping(value = "/user/{msrl}")
    @ApiOperation(value = "회원 조회", notes = "User ID로 회원을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
    })
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable long msrl) {
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElse(null));
    }

    @PostMapping(value = "/user")
    @ApiOperation(value = "회원 등록", notes = "회원을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
    })
    public SingleResult<User> save(
            @ApiParam(value = "회원 ID", required = true)  @RequestParam String uid,
            @ApiParam(value = "회원 이름", required = true) @RequestParam String name
    ) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @PutMapping(value = "/user")
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
    })
    public SingleResult<User> modify(
            @ApiParam(
                    value = "회원 번호", required = true,
                    name = "User Number", type = "long", example = "0"
            ) @RequestParam long msrl,
            @ApiParam(value = "회원 ID", required = true) @RequestParam String uid,
            @ApiParam(value = "회원 이름", required = true) @RequestParam String name
    ) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @DeleteMapping(value = "/user/{msrl}")
    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 200, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 200, message = "The resource you were trying to reach is not found")
    })
    public CommonResult delete(
            @ApiParam(
                    value = "회원 번호", required = true,
                    name = "User Number", type = "long", example = "0"
            )
            @PathVariable long msrl
    ) {
        userJpaRepo.deleteById(msrl);
        return responseService.getSuccessResult();
    }
}
