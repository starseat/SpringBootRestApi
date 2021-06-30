package com.rest.api.controller.v1.swat;

import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.entity.swat.SwatAgent;
import com.rest.api.entity.swat.SwatAgentSkill;
import com.rest.api.model.response.ListResult;
import com.rest.api.model.response.MultiResult;
import com.rest.api.model.response.SingleResult;
import com.rest.api.repo.swat.SwatAgentJpaRepo;
import com.rest.api.repo.swat.SwatAgentSkillJpaRepo;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 내부 테스트용
 */
@Api(tags = {"SWAT - Agent"})  // swagger 설정
@RequiredArgsConstructor  // 이거 대신 선언된 객체에 @Autowired 사용해도 됨.
@RestController // 결과값을 JSON으로 출력합니다.
@RequestMapping(value = "/swat/v1")
public class SwatAgentController {

    private final SwatAgentJpaRepo agentJpaRepo;
    private final SwatAgentSkillJpaRepo agentSkillJpaRepo;
    private final ResponseService responseService;  // 결과 처리 response

    @GetMapping(value = "/agents")
    @ApiOperation(value = "모든 상담사 조회", notes = "모든 상담사를 조회한다.")
    public MultiResult<SwatAgent> findAllAgent(
            @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
    ) {
        return responseService.getMultiResult(agentJpaRepo.findAll());
    }

    @GetMapping(value = "/agent/{agentId}")
    @ApiOperation(value = "상담사 조회", notes = "Agent ID로 상담사를 조회한다.")
    public SingleResult<SwatAgent> findAgentById(
            @ApiParam(value = "Agent ID", required = true) @PathVariable long agentId,
            @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
    ) {
        return responseService.getSingleResult(agentJpaRepo.findById(agentId).orElseThrow(UserNotFoundException::new));
    }

    @GetMapping(value = "/agent/{agentId}/skill")
    @ApiOperation(value = "상담사의 skill 조회", notes = "Agent ID로 상담사의 skill을 조회한다.")
    public MultiResult<SwatAgentSkill> findAgentSkillByAgentId(
            @ApiParam(value = "Agent ID", required = true) @PathVariable long agentId,
            @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
    ) {
        return responseService.getMultiResult(agentSkillJpaRepo.getAgentSkillByAgentId(agentId));
    }
}
