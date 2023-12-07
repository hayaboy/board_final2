package org.zerock.b01.domain;


import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member {
    @Id
    private String mid;


    private String mpw;
    private String email;
    private boolean del;

    private boolean social;


    @ElementCollection(fetch = FetchType.LAZY)  //기본 유형 또는 포함 가능한 클래스의 인스턴스 컬렉션을 지정합니다. 컬렉션 테이블을 통해 컬렉션을 매핑하려면 지정해야 합니다.
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpw ){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){this.social = social;}
}
