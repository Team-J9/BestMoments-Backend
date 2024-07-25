package com.j9.bestmoments.domain;

import com.j9.bestmoments.constants.TokenExpiration;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "RefreshToken", timeToLive = TokenExpiration.REFRESH_TOKEN)
public class RefreshToken extends Token {

    public RefreshToken(Member member, String token) {
        super(member, token);
    }

}
