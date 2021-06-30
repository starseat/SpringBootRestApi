package com.rest.api.repo.swat;

import com.rest.api.entity.swat.SwatAgent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwatAgentJpaRepo extends JpaRepository<SwatAgent, Long> {}

