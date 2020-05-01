package com.vipul.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private UserDetailsService myUserDetailsService;
	
	@Autowired
	JwtUtil util;
	
	@RequestMapping({ "/hello" })
	public String homePage()
	{
		return ("Hello brother");
	}
	
	@RequestMapping(value = "/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception
	{
		try
		{
			Authentication a1 =  authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		System.out.println("banta principal :: "+ a1.getPrincipal());	;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("Incorrect Credentials ");
		}
		
		UserDetails details = myUserDetailsService.loadUserByUsername(request.getUsername());
		
		final String token = util.generateToken(details); 
				
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
}
