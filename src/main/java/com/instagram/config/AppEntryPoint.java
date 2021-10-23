package com.instagram.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagram.response.ErrorResponse;

@Component
public class AppEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -2128516571707979473L;

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException authenticationException) throws IOException, ServletException {
		
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		ErrorResponse response = new ErrorResponse();
		response.setErrorMessage("UnAuthorized");
		response.setTimestamp(new Date(System.currentTimeMillis()));
		httpServletResponse.getOutputStream().write(new ObjectMapper().writeValueAsBytes(response));
	}

}
