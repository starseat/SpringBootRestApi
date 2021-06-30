package com.rest.api.repo.swat;

import com.rest.api.entity.swat.SwatAgent;
import com.rest.api.entity.swat.SwatAgentSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SwatAgentSkillJpaRepo extends JpaRepository<SwatAgentSkill, Long> {

    @Query(
            value = "SELECT agent_id, skillset_id, priority, skill_level FROM tb_ic_skillagent WHERE agent_id = :agentId",
            nativeQuery = true)
    List<SwatAgentSkill> getAgentSkillByAgentId(long agentId);
}