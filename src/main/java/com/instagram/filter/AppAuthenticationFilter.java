package com.instagram.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.instagram.entity.UserEntity;
import com.instagram.service.UserService;
import com.instagram.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AppAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtils jwtUtils;

	private final static String TOKEN_PREFIX = "Bearer ";
	
	private static final String ROLE_USER = "ROLE_USER"; 

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String jwtToken = Objects.nonNull(authHeader) && authHeader.startsWith(TOKEN_PREFIX) ? authHeader.substring(7)
				: null;
		String username = getUsernameFromToken(authHeader, jwtToken);

		if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
			UserEntity user = userService.findUserByEmail(username);
			if (jwtUtils.validateJwt(jwtToken, user)) {
				UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
						user, null, Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
				WebAuthenticationDetails authDetails = new WebAuthenticationDetailsSource().buildDetails(request);
				userPassAuthToken.setDetails(authDetails);
				SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String getUsernameFromToken(String authHeader, String jwtToken) {
		if (Objects.nonNull(authHeader)) {
			try {
				return jwtUtils.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException | ExpiredJwtException ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		return null;
	}

}
