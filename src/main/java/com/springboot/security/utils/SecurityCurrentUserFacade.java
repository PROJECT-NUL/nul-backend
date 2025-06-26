package com.springboot.security.utils;

import org.springframework.stereotype.Component;

/** Default implementation that delegates to SecurityConstants. */
@Component
public class SecurityCurrentUserFacade implements CurrentUserFacade {
    @Override
    public String getCurrentUsername() {
        return SecurityConstants.getAuthenticatedUsername();
    }
}
