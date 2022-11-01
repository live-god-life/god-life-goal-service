package com.godlife.goalservice.domain.enums;

public enum Category {
	CAREER("커리어"), HOBBY("취미"), EXERCISE("운동"), GAME("게임");
	private String categoryName;

	Category(String categoryName) {
		this.categoryName = categoryName;
	}
}
