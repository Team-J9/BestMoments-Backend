package com.j9.bestmoments.auth.oauth.service;

import com.j9.bestmoments.auth.oauth.dto.request.OAuthUserInfoDto;

public interface OAuthService {

    OAuthUserInfoDto getUserInfo(String code);

}
