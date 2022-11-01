package com.godlife.goalservice.domain.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StringListConverterTest {
	private StringListConverter stringListConverter = new StringListConverter();

	@Test
	@DisplayName("리스트를 문자로 변환")
	void convertToDatabaseColumn() {
		//given
		List<String> data = List.of("월", "수", "금");
		//when
		String result = stringListConverter.convertToDatabaseColumn(data);
		//then
		assertThat(result).isEqualTo("월,수,금");
	}

	@Test
	@DisplayName("문자를 리스트로 변환")
	void convertToEntityAttribute() {
		//given
		String data = "월,수,금";
		//when
		List<String> result = stringListConverter.convertToEntityAttribute(data);
		//then
		assertThat(result).containsExactly("월", "수", "금");
	}
}
