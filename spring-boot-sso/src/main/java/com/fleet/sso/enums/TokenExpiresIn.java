package com.fleet.sso.enums;

public enum TokenExpiresIn {

	// 设置accessToken与refreshToken过期时间（单位：毫秒）
	EXPIRES_IN(72000000L), REFRESH_EXPIRES_IN(2592000000L);

	private TokenExpiresIn(Long msec) {
		this.msec = msec;
	}

	private Long msec;

	public Long getMsec() {
		return msec;
	}

	public void setMsec(Long msec) {
		this.msec = msec;
	}

}
