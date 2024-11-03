package com.education.flashEng.security;

import com.education.flashEng.exception.JwtExceptionHandler;
import com.education.flashEng.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private SecurityPermitAllHttp securityPermitAllHttp;

    @Autowired
    private JwtExceptionHandler jwtExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = getJwtFromRequest(request);
        if (securityPermitAllHttp.getPermitAllEndpoints().contains(requestURI)) {
            filterChain.doFilter(request, response);
        }
        else{
            try{
                if (token == null) {
                    jwtExceptionHandler.handleJwtException(response, HttpStatus.UNAUTHORIZED, "Token is missing");
                    return;
                }
                jwtUtil.verifyToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                jwtExceptionHandler.handleJwtException(response, HttpStatus.UNAUTHORIZED, "Token Expired");
            } catch (MalformedJwtException e) {
                jwtExceptionHandler.handleJwtException(response, HttpStatus.BAD_REQUEST, "Token is not valid");
            } catch (Exception e) {
                jwtExceptionHandler.handleJwtException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error " + e.getMessage());
            }
        }
    }



    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
