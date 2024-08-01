package com.j9.bestmoments.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.j9.bestmoments.dto.response.OAuthUserInfoDto;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService implements OAuthService {

    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.kakao.token-uri}")
    private String tokenUri;

    @Value("${oauth2.kakao.userinfo-uri}")
    private String userinfoUrl;

    @Override
    public OAuthUserInfoDto getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        Map attributes = getUserInfoAttributes(accessToken);
        Map kakao_account = (Map) attributes.get("kakao_account");
        Map profile = (Map) kakao_account.get("profile");
        return OAuthUserInfoDto.builder()
                .provider("kakao")
                .id(attributes.get("id").toString())
                .name(profile.get("nickname").toString())
                .email(kakao_account.get("email").toString())
                .build();
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("grant_type", Collections.singletonList("authorization_code"));
        params.put("client_id", Collections.singletonList(clientId));
        params.put("redirect_uri", Collections.singletonList(redirectUri));
        params.put("code", Collections.singletonList(code));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();

        return accessTokenNode.get("access_token").asText();
    }

    private Map getUserInfoAttributes(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(userinfoUrl, HttpMethod.GET, entity, Map.class).getBody();
    }

}
