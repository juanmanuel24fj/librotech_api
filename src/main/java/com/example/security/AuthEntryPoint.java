package com.example.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.addHeader("Content-Type", "application/json");
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	    final Map<String, Object> body = new HashMap<>();
	    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
	    body.put("error", "Unauthorized");
	    body.put("message", authException.getMessage());
	    body.put("path", request.getServletPath());

	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(response.getOutputStream(), body);
		
	}

}
