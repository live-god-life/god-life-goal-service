package com.godlife.goalservice.utils.restdoc;

import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class DocumentProvider {
    public static RequestFieldsSnippet getPostGoalsRequestFieldsSnippet() {
        return requestFields(
                fieldWithPath("title").description("목표 제목"),
                fieldWithPath("categoryName").description("목표 카테고리명"),
                fieldWithPath("categoryCode").description("목표 카테고리코드"),

                fieldWithPath("mindsets[].content").description("마인드셋 내용"),

                fieldWithPath("todos[].title").description("목표 제목"),
                fieldWithPath("todos[].type").description("목표 제목"),
                fieldWithPath("todos[].depth").description("목표 제목"),
                fieldWithPath("todos[].order").description("목표 제목"),
                fieldWithPath("todos[].todos").optional().description("목표 제목"),

                fieldWithPath("todos[].todos[].title").description("목표 제목"),
                fieldWithPath("todos[].todos[].type").description("목표 제목"),
                fieldWithPath("todos[].todos[].depth").description("목표 제목"),
                fieldWithPath("todos[].todos[].order").description("목표 제목"),
                fieldWithPath("todos[].todos[].todos").optional().description("목표 제목"),

                fieldWithPath("todos[].todos[].todos[].title").description("목표 제목"),
                fieldWithPath("todos[].todos[].todos[].type").description("목표 제목"),
                fieldWithPath("todos[].todos[].todos[].depth").description("목표 제목"),
                fieldWithPath("todos[].todos[].todos[].order").description("목표 제목"),
                fieldWithPath("todos[].todos[].todos[].todos").optional().description("목표 제목")
        );
    }

    public static ResponseFieldsSnippet getSuccessResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("status").description("api 응답 상태"),
                fieldWithPath("message").description("api 응답 메시지"),
                fieldWithPath("data").description("api 응답 데이터")
        );
    }
}
