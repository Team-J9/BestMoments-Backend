package com.j9.bestmoments.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);

}
