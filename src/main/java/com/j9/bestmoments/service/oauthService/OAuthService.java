package com.j9.bestmoments.service.oauthService;

import com.j9.bestmoments.dto.response.OAuthUserInfoDto;

public interface OAuthService {

    OAuthUserInfoDto getUserInfo(String code);

}
