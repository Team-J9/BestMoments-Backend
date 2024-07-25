package com.j9.bestmoments.domain;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("Token")
public class Token {

    @Id
    private String token;
    private UUID memberId;

    public Token(Member member, String token) {
        this.token = token;
        this.memberId = member.getId();
    }

}
