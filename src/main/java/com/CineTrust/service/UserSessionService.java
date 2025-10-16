package com.CineTrust.service;

import com.CineTrust.entity.UserSession;
import com.CineTrust.repository.UserSessionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSessionService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    // When a user logs in (token generated)
    public void createSession(Long userId, String token) {
        UserSession session = new UserSession();
        session.setUserId(userId);
        session.setJwtToken(token);
        session.setValid(true);
        session.setLoginTime(LocalDateTime.now());
        userSessionRepository.save(session);
    }

    // Check if token is valid (for JwtAuthFilter)
    public boolean isTokenValid(String token) {
        return userSessionRepository.findByJwtToken(token)
                .map(UserSession::isValid)
                .orElse(false);
    }

    // Invalidate on logout
    public boolean invalidateToken(String jwtToken) {
        return userSessionRepository.findByJwtToken(jwtToken)
                .map(session -> {
                    session.setValid(false);
                    session.setLogoutTime(LocalDateTime.now());
                    userSessionRepository.save(session);
                    return true;
                })
                .orElse(false);
    }
}

