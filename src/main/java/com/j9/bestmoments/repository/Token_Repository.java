package com.j9.bestmoments.repository;

import com.j9.bestmoments.domain.Token_;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Token_Repository extends CrudRepository<Token_, String> {

    boolean existsByToken(String token);

}
