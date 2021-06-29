package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

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
			//			BufferedReader br = request.getReader();
			//			String input = null;
			//			while((input = br.readLine()) != null) {
			//				System.out.println(input);
			//			}
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);

			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

			//2. 정상인지 로그인 시도를 해보는 것authenticationManager로 로그인 시도를 하면 principaldetailsservice가 호출이됨 그럼 loaduserbyusername이 자동으로 실행됨
			//DB에 있는 username과 password가 일치한다.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			//authentication 객체가 session 영역에 저장됨. => 로그인이 되었다는 뜻
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.println("로그인 완료됨"+principalDetails.getUser().getUsername());//로그인이 정상적으로 되었다는 것
			//authentication 객체가 session 영역에 저장해야하고, 그방법이 return 해주는 것이다.
			//리턴해주는 이유는 권한관리를 security가 대신해주기 때문에 편하려고 하는 것
			//굳이 JWT토큰을 사용하면서 세션을 만들이유가 없음
			//단지 권한처리 때문에 세션에 넣어줌
			
			return authentication;
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("===========================");

		return null;
	}
	
	//attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨
	//JWT 토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면 됨
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		super.successfulAuthentication(request, response, chain, authResult);
	}
}
