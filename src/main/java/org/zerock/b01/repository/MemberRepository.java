package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Member;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {


    // 로그인 시에 MemberRole을 같이 로딩하도록 메서드 추가

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(String mid);


    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);


    @Modifying  //`@Modifying` 주석이 필요한 쿼리에는 INSERT, UPDATE, DELETE 및 DDL 문이 포함
    @Transactional
    @Query("update Member m set m.mpw =:mpw where m.mid = :mid ")
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);








}