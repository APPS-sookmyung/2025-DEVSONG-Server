package com.devsong.server.jwt;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //회원가입, 로그인 api 요청은 토큰 없이 가능하도록 허용
                        .requestMatchers("/user/signup", "/user/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}