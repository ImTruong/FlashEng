package com.education.flashEng.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SecurityPermitAllHttp {
    private static final Set<String> PERMIT_ALL_ENDPOINTS = new HashSet<>();

    static {
        PERMIT_ALL_ENDPOINTS.add("/user/login");
        PERMIT_ALL_ENDPOINTS.add("/user/register");
    }

    public static Set<String> getPermitAllEndpoints() {
        return PERMIT_ALL_ENDPOINTS;
    }
}
