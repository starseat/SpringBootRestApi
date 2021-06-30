package com.rest.api.entity.swat;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder // builder를 사용할수 있게 합니다.
@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "tb_ic_skillagent")
public class SwatAgentSkill {

    @Id
    @Column(name = "agent_id", nullable = false, unique = true)
    private long agentId;

    @Column(name = "skillset_id", nullable = false, unique = true)
    private long skillSetId;

    @Column(name = "priority", nullable = false)
    private long priority;

    @Column(name = "skill_level", nullable = false)
    private long skillLevel;
}
