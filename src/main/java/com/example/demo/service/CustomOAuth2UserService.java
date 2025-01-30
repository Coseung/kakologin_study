package com.example.demo.service;

import com.example.demo.dto.OAuthAttributes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

// 스프링에서 서비스 클래스임을 명시하는 어노테이션
@Service
// OAuth2UserService 인터페이스를 구현하여 OAuth2 로그인 처리를 커스터마이징
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    // OAuth2UserRequest를 통해 OAuth2User 객체를 생성하는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        // OAuth2UserService를 통해 OAuth2User 정보를 가져옴
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // 현재 로그인 진행 중인 서비스(카카오)를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        // OAuth2 로그인 진행 시 키가 되는 필드값 (PK와 같은 의미)
        // 카카오 로그인에서는 "id" 값을 의미
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        
        // OAuth2UserService를 통해 가져온 OAuth2User의 속성을 담을 클래스
        // 카카오 사용자 정보를 우리 서비스에 맞게 변환하는 과정
        OAuthAttributes attributes = OAuthAttributes.of(
            registrationId, 
            userNameAttributeName, 
            oAuth2User.getAttributes()
        );
        
        // DefaultOAuth2User 객체를 생성하여 반환
        return new DefaultOAuth2User(
                // 사용자의 권한을 설정 (여기서는 ROLE_USER로 고정)
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                // 사용자의 속성 정보
                attributes.getAttributes(),
                // 사용자의 키가 되는 필드 값
                attributes.getNameAttributeKey());
    }
} 