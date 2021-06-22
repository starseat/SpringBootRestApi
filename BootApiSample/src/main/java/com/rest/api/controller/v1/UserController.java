package com.rest.api.controller.v1;

import com.rest.api.entity.User;
import com.rest.api.repo.UserJpaRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"User"})  // swagger 설정
@RequiredArgsConstructor  // 이거 대신 선언된 객체에 @Autowired 사용해도 됨.
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;

    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회한다.")
    @GetMapping(value = "/users")
    public List<User> findAllUser() {
        return userJpaRepo.findAll();
    }

    @ApiOperation(value = "회원 조회", notes = "회원을 조회한다.")
    @GetMapping(value = "/user")
    public User findUser(
            @ApiParam(value = "회원 ID", required = true)  @RequestParam String _uid
    ) {
        User user = new User();
        user.setUid(_uid);
        user.setName("test");

        return user;
    }

    @ApiOperation(value = "회원 등록", notes = "회원을 등록한다.")
    @PostMapping(value = "/user")
    public User save(
            @ApiParam(value = "회원 ID", required = true)  @RequestParam String _uid,
            @ApiParam(value = "회원 이름", required = true) @RequestParam String _name
    ) {
        User user = User.builder()
                .uid(_uid)
                .name(_name)
                .build();
        return userJpaRepo.save(user);
    }
}
