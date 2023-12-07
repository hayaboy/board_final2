package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
//    private PasswordEncoder passwordEncoder;

//    public CustomUserDetailsService(){
//        this.passwordEncoder=new BCryptPasswordEncoder();
//    }


    @Override  //UserDetails 사용자 인증과 관련된 정보들을 저장
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + username);

//        UserDetails userDetails =User.builder().username("user1").password("1111").authorities("ROLO_USER").build();
//        UserDetails userDetails =User.builder().username("user1").password(passwordEncoder.encode("1111")).authorities("ROLE_USER").build();

        Optional<Member> result = memberRepository.getWithRoles(username);

        if (result.isEmpty()) { //해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("username not found...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO =new MemberSecurityDTO(member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" +memberRole.name())).collect(Collectors.toList())
                );

//        return userDetails;

        log.info("memberSecurityDTO : " + memberSecurityDTO);
        return memberSecurityDTO;
    }
}
