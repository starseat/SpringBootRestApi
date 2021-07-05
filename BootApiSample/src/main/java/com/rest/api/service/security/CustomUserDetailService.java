package com.rest.api.service.security;

import com.rest.api.advice.exception.UserNotFoundException;
import com.rest.api.repo.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepo userJpaRepo;

    /**
     * token 에 세팅된 유저정보로 회원정보 조회기능 재정의
     * @param userPk
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
//    @Cacheable(value = CaachKey.USER, key = "#userPk", unless = "#result == null")
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userJpaRepo.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new);
    }
}
