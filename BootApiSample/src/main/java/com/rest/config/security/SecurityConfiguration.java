package com.rest.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 보안 설정 적용
 *  - anyRequest().hasRole("USER") 와 anyRequest().authenticated() 는 동일한 동작을 함.
 *  - UsernamePasswordAuthenticationFilter 앞에 설정 해야 함.
 *  - SpringSecurity 적용 후에는 모든 리소스에 대한 접근이 제한되므로, Swagger 페이지는 예죄 적용 해야 페이지 접근 가능
 *  - 리소스 접근 제한 표현식
 *    + hasIpAddress(ip) : 접근자의 IP주소가 매칭되는지 확인
 *    + hasRole(role) : 역할이 부여된 권한(Granted Authority)과 일치하는지 확인
 *    + hasAnyRole(role) : 부여된 역할 중 일치하는 항목이 있는지 확인
 *                       ex) access = "hasAnyRole('ROLE_USER','ROLE_ADMIN')"
 *    + permitAll : 모든 접근자를 항상 승인
 *    + denyAll : 모든 접근자 거부
 *    + anonymous : 사용자가 익명 사용자 인지 확인
 *    + authenticated : 인증된 사용자인지 확인
 *    + rememberMe : 사용자가 remember me를 사용해 인증했는지 확인
 *    + fullyAuthenticated : 사용자가 모든 크리덴셜을 갖춘 상태에서 인증했는지 확인
 */

/**
 * SpringSecurity Filter
 *  - 기능별 필터의 집합으로 되어있음.
 *  - client가 리소스 요청시 접근 권한이 없는 경우 기본적으로 로그인 폼으로 보내는데 그 역할의 필터는 UsernamePasswordAuthenticationFilter 임.
 *  - Reset API 에선 로그인 폼이 따로 없으므로 인증 권한이 없을 시 UsernamePasswordAuthenticationFilter 전에 처리하여야 함.
 *  - 필터의 순서
 *    +  1. ChannelProcessingFilter
 *    +  2. SecurityContextPersistenceFilter
 *    +  3. ConcurrentSessionFilter
 *    +  4. HeaderWriterFilter
 *    +  5. CsrfFilter
 *    +  6. LogoutFilter
 *    +  7. X509AuthenticationFilter
 *    +  8. AbstractPreAuthenticatedProcessingFilter
 *    +  9. CasAuthenticationFilter
 *    + 10. UsernamePasswordAuthenticationFilter
 *    + 11. BasicAuthenticationFilter
 *    + 12. SecurityContextHolderAwareRequestFilter
 *    + 13. JaasApiIntegrationFilter
 *    + 14. RememberMeAuthenticationFilter
 *    + 15. AnonymousAuthenticationFilter
 *    + 16. SessionManagementFilter
 *    + 17. ExceptionTranslationFilter
 *    + 18. FilterSecurityInterceptor
 *    + 19. SwitchUserFilter
 */

// 권한 설정이 필요한 리소스에 @PreAuthorize, @Secured 로 권한 세팅
// @PreAuthorize : 표현식 사용가능. ex) @PreAuthorize(“hasRole(‘ROLE_USER’) and hasRole(‘ROLE_ADMIN’)”)
// @Secured : 표현식 사용 불가능. ex) @Secured({“ROLE_USER”,”ROLE_ADMIN”})
// annotation으로 권한설정시 GlobalMethodSecurity를 활성화 해야 함.
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()  // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 됨.
                .csrf().disable()  // rest api 이므로 csrf 보안 필요없음. 따라서 disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // jwt token 인증할것이므로 세션필요없음. 생성안함.

                // @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 로 대체함
                .and()
                .authorizeRequests()  // 다음 Requst에 대한 사용권한 체크
                .antMatchers(
                        "/*/signin", "/*/signin/**",
                        "/*/signup", "/*/signup/**",
                        "/social/**").permitAll()  // 가입 및 인증 주소는 누구나 접근 가능
                .antMatchers(
                        HttpMethod.GET,
                        "/exception/**", "/helloworld/**",
                        "/actuator/health", "/v1/board/**",
                        "/swat/**",
                        "/favicon.ico").permitAll()  // 등록된 GET요청 리소스는 누구나 접근 가능
//                .antMatchers("/*/users").hasRole("ADMIN")  // 테스트용으로 /users api 는 ROLE_ADMIN 권한만 접근 가능하게
                .anyRequest().hasRole("USER")  // 그 외 나머지 요청은 모두 인증된 회원만 접근 가능

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                // jwt 토큰 필터를 id/password 인증 필터 전에 넣음
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;
    }

    /**
     * ignore swagger security config
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/swagger/**", "/swagger-ui.html", "/swagger-resources/**",
                "/v2/api-docs", "/webjars/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
