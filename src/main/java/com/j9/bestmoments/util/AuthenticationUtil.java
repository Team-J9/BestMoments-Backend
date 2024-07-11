package com.j9.bestmoments.util;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthenticationUtil {

    public static UUID getMemberId() {
        return UUID.fromString(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString()
        );
    }

}
