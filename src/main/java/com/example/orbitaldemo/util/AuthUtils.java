package com.example.orbitaldemo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtils {

    public static Jwt getPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Object getClaim(String claim) {
        return getPrincipal().getClaim(claim);
    }

    public static Long getUserId() {
        return (Long) getClaim("user_id");
    }

}
