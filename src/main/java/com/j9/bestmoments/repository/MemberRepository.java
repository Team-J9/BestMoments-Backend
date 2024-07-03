package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);

}
