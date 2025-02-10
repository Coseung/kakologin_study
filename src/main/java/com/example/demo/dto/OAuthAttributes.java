// package com.example.demo.dto;

// import lombok.Builder;
// import lombok.Getter;

// import java.util.Map;

// @Getter
// @Builder
// public class OAuthAttributes {
//     // OAuth2 제공자(카카오)에서 받아온 원본 속성값들을 저장
//     private Map<String, Object> attributes;
//     // OAuth2 제공자가 사용하는 식별자 속성 이름
//     private String nameAttributeKey;
//     // 사용자 이름
//     private String name;
//     // 사용자 프로필 이미지 URL
//     private String picture;

//     // 소셜 로그인 정보를 처리하는 메서드
//     public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
//         if("kakao".equals(registrationId)) {
//             return ofKakao(userNameAttributeName, attributes);
//         }
//         return null;
//     }

//     // 카카오 인증 정보를 처리하는 메서드
//     private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
//         // 카카오 계정 정보를 가져옴
//         Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//         // 카카오 프로필 정보를 가져옴
//         Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

//         // OAuthAttributes 객체를 생성하여 카카오 프로필 정보를 설정
//         return OAuthAttributes.builder()
//                 .name((String) profile.get("nickname"))
//                 .picture((String) profile.get("profile_image_url"))
//                 .attributes(attributes)
//                 .nameAttributeKey(userNameAttributeName)
//                 .build();
//     }
// } 


package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

// Getter 메소드를 자동으로 생성하는 롬복 어노테이션
@Getter
// Builder 패턴을 자동으로 생성하는 롬복 어노테이션
@Builder
public class OAuthAttributes {
    // OAuth2 제공자(카카오)에서 받아온 원본 속성값들을 저장
    private Map<String, Object> attributes;
    
    // OAuth2 로그인 진행 시 키가 되는 필드값
    private String nameAttributeKey;
    
    // 사용자 이름
    private String name;
    
    // 사용자 프로필 사진 URL
    private String picture;

    // OAuth2 제공자(카카오)별로 속성을 변환하는 메소드
    public static OAuthAttributes of(String registrationId, 
                                   String userNameAttributeName, 
                                   Map<String, Object> attributes) {
        // registrationId가 "kakao"인 경우 카카오 사용자 정보 변환
        if("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return null;
    }

    // 카카오 사용자 정보를 변환하는 메소드
    private static OAuthAttributes ofKakao(String userNameAttributeName, 
                                         Map<String, Object> attributes) {
        // kakao_account에 사용자 정보가 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // kakao_account 안에 profile이라는 JSON 객체가 있음
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // Builder 패턴을 사용하여 OAuthAttributes 객체 생성 Service에서 보낸값을 가공해서 리턴 해주는 역할
        return OAuthAttributes.builder()
                // 카카오 프로필에서 닉네임을 가져와 name에 저장
                .name((String) profile.get("nickname"))
                // 카카오 프로필에서 프로필 이미지를 가져와 picture에 저장
                .picture((String) profile.get("profile_image_url"))
                // 카카오가 제공하는 모든 속성값들을 저장
                .attributes(attributes)
                // OAuth2 로그인 진행 시 키가 되는 필드값을 저장
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}