package com.j9.bestmoments.service;

import com.j9.bestmoments.repository.Token_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Token_Service {

    private final Token_Repository tokenRepository;

}
