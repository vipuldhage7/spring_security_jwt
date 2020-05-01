package com.vipul.security.jwt;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.savedrequest.Enumerator;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException 
	{
/*					String authorizationHeader = req.getHeader("Authorization");
					System.out.println(req.getParameter("Authorization"));*/
					String authorizationHeader = req.getParameter("Authorization");
					
					
					String jwt = null;
					String username = null;
					
					System.out.println("authorizationHeader --> "+ authorizationHeader);
					
					if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
					{
						jwt = authorizationHeader.substring(7);
						username = jwtUtil.extractUsername(jwt);
						
					}
					
					if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
					{
						UserDetails details = this.userDetailsService.loadUserByUsername(username);
						
						if(jwtUtil.validateToken(jwt, details))
						{
							UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
			                        details, null, details.getAuthorities());
							
			                usernamePasswordAuthenticationToken
			                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
			                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
						}
					}
					
					chain.doFilter(req, res);
		
	}
}
