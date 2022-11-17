package com.godlife.goalservice.domain.enums;

public enum Category {
	LIFESTYLE("생활습관"),
	WORKOUT("운동"),
	CAREER("커리어"),
	FINANCE("돈관리"),
	HOBBY("취미"),
	LEARNING("학습"),
	MINDFULNESS("마음챙김"),
	SELFCARE("셀프케어");

	private String categoryName;

	Category(String categoryName) {
		this.categoryName = categoryName;
	}
}
