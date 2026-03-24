package com.pranavrajkota.library2026.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService,
                         CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if(username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if(jwtService.isTokenValid(token, userDetails)){// jwtService.getKey(token,userDetails) // again this line full of errors

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}








// this is written by me -------------------------------------------------------------------
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter { // Java web token   authentication   filter
//
//    private final JwtService jwtService;
//
//    public JwtAuthFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = authHeader.substring(7);
//        String username = jwtService.extractUsername(token);
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(
//                            username,
//                            null,
//                            Collections.emptyList()
//                    );
//            authToken.setDetails(
//                    new WebAuthenticationDetailsSource().buildDetails(request)
//            );
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}