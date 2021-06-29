package com.cos.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;


//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 해당 필터가 있음
// /login 요청해서 usernameㅇ랑 password를 전송하면 post
//이 필터가 동작함

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		//1. username, password 받아서
		try {
			BufferedReader br = request.getReader();
			String input = null;
			while((input = br.readLine()) != null) {
				System.out.println(input);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("===========================");
		//2. 정상인지 로그인 시도를 해보는 것authenticationManager로 로그인 시도를 하면 
		//principaldetailsservice가 호출이됨 
		//그럼 loaduserbyusername이 자동으로 실행됨
		
		//3. principadetails를 세션에 담고(권한 관리를 위한 것 ex)v1)
		//4. JWT 토큰을 만들어서 응답해주면됨
		return super.attemptAuthentication(request, response);
	}
}
