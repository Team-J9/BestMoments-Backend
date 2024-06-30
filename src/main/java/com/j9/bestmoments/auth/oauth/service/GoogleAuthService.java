package com.j9.bestmoments.auth.oauth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.j9.bestmoments.auth.oauth.dto.request.OAuthUserInfoDto;
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
public class GoogleAuthService implements OAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.registration.google.userinfo-uri}")
    private String userinfoUrl;

    @Override
    public OAuthUserInfoDto getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        Map attributes = getUserInfoAttributes(accessToken);
        return OAuthUserInfoDto.builder()
                .provider("google")
                .id(attributes.get("id").toString())
                .name(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .profileImageUrl(attributes.get("picture").toString())
                .build();
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("code", Collections.singletonList(code));
        params.put("client_id", Collections.singletonList(clientId));
        params.put("client_secret", Collections.singletonList(clientSecret));
        params.put("redirect_uri", Collections.singletonList(redirectUri));
        params.put("grant_type", Collections.singletonList("authorization_code"));

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
