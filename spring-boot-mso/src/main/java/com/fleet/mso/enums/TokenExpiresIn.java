package com.fleet.mso.enums;

public enum TokenExpiresIn {

    // 设置 accessToken 与 refreshToken 过期时间 (单位：秒)
    ACCESS_EXPIRES_IN(60L), REFRESH_EXPIRES_IN(5184000L);

    TokenExpiresIn(Long sec) {
        this.sec = sec;
    }

    private Long sec;

    public Long getSec() {
        return sec;
    }

    public void setSec(Long sec) {
        this.sec = sec;
    }
}
