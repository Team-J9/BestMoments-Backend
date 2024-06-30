package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);

}
