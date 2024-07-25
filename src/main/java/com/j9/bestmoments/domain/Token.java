package com.j9.bestmoments.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@RedisHash(value = "Token")
public
class Token {

    @Id
    private String token;

    private TokenType tokenType;

    @TimeToLive
    private Long expiration;

    private UUID memberId;

    @Builder
    public Token(Member member, TokenType tokenType, String token) {
        this.memberId = member.getId();
        this.token = token;
        this.tokenType = tokenType;
        this.expiration = tokenType.getExpiration();
    }

}
