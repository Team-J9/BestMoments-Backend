package com.j9.bestmoments.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    private String refreshToken;
    private String accessToken;
    private Boolean isExpired = false;

    @Builder
    public Token(Member member, String refreshToken, String accessToken) {
        this.member = member;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void expire() {
        this.isExpired = true;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
