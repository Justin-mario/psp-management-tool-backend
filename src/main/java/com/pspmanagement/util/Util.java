package com.pspmanagement.util;

import com.pspmanagement.repository.ProjectTimeLogRepository;
import org.springframework.stereotype.Component;



@Component
public class Util {

    public String extractJwtToken(String authHeader) {
        // Check if the header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token (remove "Bearer " prefix)
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

}
