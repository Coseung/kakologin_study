package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.service.CustomOAuth2UserService;

// Spring Security 설정을 위한 클래스임을 나타내는 어노테이션
@Configuration
// Spring Security를 활성화하는 어노테이션
@EnableWebSecurity
public class SecurityConfig {

    // OAuth2 사용자 정보를 처리하는 서비스
    private final CustomOAuth2UserService customOAuth2UserService;

    // 생성자를 통한 의존성 주입
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // Spring Security의 필터 체인을 구성하는 메소드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화
            .csrf().disable()
            
            // URL 별 접근 권한 설정
            .authorizeHttpRequests()
                // "/" 와 "/login/**" 경로는 모든 사용자가 접근 가능
                .requestMatchers("/", "/login/**").permitAll()
                // 그 외의 모든 요청은 인증된 사용자만 접근 가능
                .anyRequest().authenticated()
            .and()
            
            // OAuth2 로그인 설정
            .oauth2Login()
                // 로그인 페이지 경로 설정
                .loginPage("/login")
                // 로그인 성공 시 리다이렉트할 경로
                .defaultSuccessUrl("/")
                // 로그인 실패 시 리다이렉트할 경로
                .failureUrl("/login?error=true")
                // OAuth2 사용자 정보 처리 설정
                .userInfoEndpoint()
                    // 사용자 정보를 처리할 서비스 지정
                    .userService(customOAuth2UserService)
            .and()
            .and()
            
            // 로그아웃 설정
            .logout()
                // 로그아웃 성공 시 리다이렉트할 경로
                .logoutSuccessUrl("/")
                // 로그아웃 시 세션 무효화
                .invalidateHttpSession(true)
                // 로그아웃 시 인증 정보 제거
                .clearAuthentication(true);
        
        // 구성된 보안 필터 체인을 반환
        return http.build();
    }
}
