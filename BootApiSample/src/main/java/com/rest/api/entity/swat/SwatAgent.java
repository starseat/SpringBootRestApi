package com.rest.api.entity.swat;

import lombok.*;

import javax.persistence.*;

@Builder // builder를 사용할수 있게 합니다.
@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "tb_ic_agentmaster")
public class SwatAgent {
    @Id
    @Column(name = "agent_id", nullable = false, unique = true)
    private long agentId;

    @Column(name = "agent_login_id", nullable = false)
    private String agentLoginId;

    @Column(name = "tenant_id", nullable = false)
    private long tenantId;

    @Column(name = "agent_name", nullable = false)
    private String agentName;

    @Column(name = "agent_alias", nullable = false)
    private String agentAlias;

    @Column(name = "group_id", nullable = false)
    private long groupId;

    @Column(name = "agent_grade", nullable = false)
    private long agentGrade;
}
