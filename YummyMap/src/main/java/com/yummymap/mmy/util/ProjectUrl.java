package com.yummymap.mmy.util;

public enum ProjectUrl {
	MAIN_VIEW ("/yummymap/index.mmy"),
	MAIN_LIST_VIEW ("/yummymap/main.mmy"),
	UPSO_DETAIL_VIEW("/yummymap/main/getDetail.mmy"),
	LOGIN_VIEW ("/yummymap/member/loginView.mmy");
	
	private String url;
	
	private ProjectUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
}

