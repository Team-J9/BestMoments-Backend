package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    boolean existsByToken(String token);

}
