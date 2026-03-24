//package com.pranavrajkota.library2026.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.sql.Date;
//
//@Component
//public class JwtUtils {
//    // In production, keep this in application.properties!
//    private String jwtSecret = "myVerySecretKeyThatIsLongEnoughToBeSecure1234567890";
//    private int jwtExpirationMs = 86400000; // 24 hours
//
//    public String generateJwtToken(Authentication authentication) {
//        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
//
//        return Jwts.builder()
//                .setSubject((userPrincipal.getUsername()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
//
//    public String extractUsername(String jwt) {
//    }
//
//    public boolean validateToken(String jwt, UserDetails userDetails) {
//    }
//}