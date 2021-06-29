package com.cos.jwt.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.filter.MyFilter3;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsFilter;
	//jwt기본 고정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다.
		.and()
		.addFilter(corsFilter)//@CrossOrigin(인증이 없을 때 문제), 시큐리티 필터에 등록 인증해줘야함
		.formLogin().disable()//form로그인방식 안씀
		.httpBasic().disable()//http로그인방식 안씀
		.addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager을 던져줘야함 WebSecurityConfigurerAdapter 가들고 있음
		.authorizeRequests()
		.antMatchers("/api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/manager/**")
		.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll();

		
	}
}
