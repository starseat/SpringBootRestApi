package com.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 의 보안을 적용하기 위해 UserDetails 상속 받아 추가정보 재정의
 * roles 는 회원이 가지고 있는 권한 정보이고, 신규 가입시 기본값으로 "ROLE_USER"가 세팅됨.
 * 권한은 계정당 여러 개를 가질 수 있으므로 Collection 으로 선언.
 *
 * getUsername 은 security에서 사용하는 회원 구분 ID 임. 여기선 uid로 변경함.
 *
 * Security에서 사용하는 회원 상태값들은 모두 사용 안하므로 true로 설정
 * JSON 결과로 출력 하지 않을 데이터는 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 선언
 *
 * Security에서 사용하는 상태값
 *  + isAccountNonExpired : 계정이 만료 안되었는지
 *  + isAccountNonLocked : 계정이 잠기지 않았는지
 *  + isCredentialsNonExpired : 계정 패스워드가 만료 안되었는지
 *  + isEnabled : 계정이 사용 가능한지
 */
@Builder // builder를 사용할수 있게 합니다.
@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "jw_boot_user") // 'jw_user' 테이블과 매핑됨을 명시
public class User implements UserDetails {

    @Id // primaryKey임을 알립니다.
    // pk생성전략을 DB에 위임한다는 의미입니다.
    // mysql로 보면 pk 필드를 auto_increment로 설정해 놓은 경우로 보면 됩니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msrl;

    // uid column을 명시합니다. 필수이고 유니크한 필드이며 길이는 32입니다.
    @Column(nullable = false, unique = true, length = 32)
    private String uid;

    // 비밀번호는 JSON 출력 결과에서 제거. Write Only
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 128)
    private String password;

    // name column을 명시합니다. 필수이고 길이는 128입니다.
    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 128)
    private String provider;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * security에서 사용하는 회원 구분 ID.
     * uid로 변경하여 사용.
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.uid;
    }

    /**
     * 계정이 만료 안되었는지 여부
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠기지 않았는지 여부
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정 패스워드가 만료 안되었는지 여부
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 사용 가능한지 여부
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
