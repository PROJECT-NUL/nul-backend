package com.springboot.configuration;

import com.springboot.security.utils.CurrentUserFacade;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * AuditorAware implementation that retrieves the current username from Spring Security.
 */
@Component("jpaAuditAware")
@RequiredArgsConstructor
public class JpaAuditAwareImpl implements AuditorAware<String> {

    private final CurrentUserFacade currentUserFacade;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.ofNullable(currentUserFacade.getCurrentUsername());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
