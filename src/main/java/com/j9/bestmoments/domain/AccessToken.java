package com.j9.bestmoments.domain;

import com.j9.bestmoments.constants.TokenExpiration;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "AccessToken", timeToLive = TokenExpiration.ACCESS_TOKEN)
public class AccessToken extends Token {

    public AccessToken(Member member, String token) {
        super(member, token);
    }

}
