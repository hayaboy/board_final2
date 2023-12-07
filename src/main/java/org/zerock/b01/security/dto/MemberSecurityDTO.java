package org.zerock.b01.security.dto;


//UserDetails
//핵심 사용자 정보를 제공합니다. 구현은 보안 목적으로 Spring Security에서 직접 사용되지 않습니다. 나중에 인증 개체에 캡슐화되는 사용자 정보를 저장하기만 하면 됩니다.
// 이를 통해 보안과 관련되지 않은 사용자 정보(이메일 주소, 전화번호 등)를 편리한 위치에 저장할 수 있습니다.



// User : UserDetails 인터페이스를 구현한 클래스로, 최대한 간단하게 UserDetails 타입을 생성할 수 있는 방법 제공
// UserDetailsService에서 검색한 핵심 사용자 정보를 모델링합니다.
//개발자는 이 클래스를 직접 사용하거나 하위 클래스로 분류하거나 처음부터 자체 UserDetails 구현을 작성할 수 있습니다.
//
//같음 및 해시코드 구현은 사용자 이름 속성만을 기반으로 합니다. 이는 동일한 사용자 주체 개체(예: 사용자 레지스트리)에
// 대한 조회가 모든 속성(예: 권한, 비밀번호 등)은 동일합니다.//
//이 구현은 변경할 수 없습니다. 인증 후 비밀번호를 삭제할 수 있도록 CredentialsContainer 인터페이스를 구현합니다.
// 인스턴스를 메모리에 저장하고 재사용하는 경우 부작용이 발생할 수 있습니다.
// 그렇다면 호출될 때마다 UserDetailsService에서 복사본을 반환해야 합니다.


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String mid;
    private String mpw;
    private String email;
    private boolean del;

    private boolean social;


    private Map<String, Object> props; //소셜 로그인 정보


    //GrantedAuthority
    // 인증 개체에 부여된 권한을 나타냅니다.GrantedAuthority는 자신을 문자열로 나타내거나
    public MemberSecurityDTO(String username, String password, String email, boolean del, boolean social, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;

    }


    public Map<String, Object> getAttributes() {
        return this.getProps();
    }


    @Override
    public String getName() {
        return this.mid;
    }
}
