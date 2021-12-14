package com.srankoin.finance.security.filters;

import antlr.StringUtils;
import com.srankoin.finance.entity.UserClass;
import com.srankoin.finance.security.util.JwtUtil;
import com.srankoin.finance.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {


    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private  UserDetailsServiceImpl userDetailsService;


    public AuthTokenFilter() {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            System.out.println("this is jwt" + jwt);
            if(jwt != null && jwtUtil.validateJwtToken(jwt)){
                String username = jwtUtil.getUserNameFromJwtToken(jwt);
                UserClass userDetails = (UserClass) userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication object" + authentication);
            }
        }
        catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");

        if(headerAuth != null && headerAuth.startsWith("Bearer")){
            return headerAuth.substring(7, headerAuth.length());

        }
        return null;
    }
}
