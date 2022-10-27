package com.godlife.goalservice.utils.restdoc;

import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

/*
    todo
    request, response fileds 를 기입하는게 중복작업이 많다고 느껴지는데 방법이없는가...
    계층형의 경우 다 기입해줘야하나....
    일단 relaxedRequestFields 활용
 */

public class DocumentProvider {
    public static RequestFieldsSnippet getPostGoalsRequestFieldsSnippet() {
        return relaxedRequestFields(
                fieldWithPath("title").description("목표 제목"),
                fieldWithPath("categoryName").description("목표 카테고리명"),
                fieldWithPath("categoryCode").description("목표 카테고리코드"),

                fieldWithPath("mindsets[].content").description("마인드셋 내용"),

                fieldWithPath("todos[].title").description("투두 제목"),
                fieldWithPath("todos[].type").description("투두 타입"),
                fieldWithPath("todos[].depth").description("투두 뎁스"),
                fieldWithPath("todos[].orderNumber").description("투두 정렬순서"),
                fieldWithPath("todos[].startDate").optional().description("투두 정렬순서"),
                fieldWithPath("todos[].endDate").optional().description("투두 정렬순서"),
                fieldWithPath("todos[].notification").optional().description("투두 정렬순서"),
                fieldWithPath("todos[].repetitionType").optional().description("투두 정렬순서"),
                fieldWithPath("todos[].repetitionParams").optional().description("투두 정렬순서")


//                fieldWithPath("todos[].title").description("투두 제목"),
//                fieldWithPath("todos[].type").description("투두 타입"),
//                fieldWithPath("todos[].depth").description("투두 뎁스"),
//                fieldWithPath("todos[].order").description("투두 정렬순서"),
//                fieldWithPath("todos[].repetitionType").description("투두 정렬순서"),
//                fieldWithPath("todos[].repetitionParams").description("투두 정렬순서"),
//                fieldWithPath("todos[].todos").optional().description("목표 제목"),
//
//                fieldWithPath("todos[].todos[].title").description("목표 제목"),
//                fieldWithPath("todos[].todos[].type").description("목표 제목"),
//                fieldWithPath("todos[].todos[].depth").description("목표 제목"),
//                fieldWithPath("todos[].todos[].order").description("목표 제목"),
//                fieldWithPath("todos[].todos[].repetitionType").description("투두 정렬순서"),
//                fieldWithPath("todos[].todos[].repetitionParams").description("투두 정렬순서"),
//                fieldWithPath("todos[].todos[].todos").optional().description("목표 제목"),
//
//                fieldWithPath("todos[].todos[].todos[].title").description("목표 제목"),
//                fieldWithPath("todos[].todos[].todos[].type").description("목표 제목"),
//                fieldWithPath("todos[].todos[].todos[].depth").description("목표 제목"),
//                fieldWithPath("todos[].todos[].todos[].order").description("목표 제목"),
//                fieldWithPath("todos[].todos[].todos[].repetitionType").description("투두 정렬순서"),
//                fieldWithPath("todos[].todos[].todos[].repetitionParams").description("투두 정렬순서"),
//                fieldWithPath("todos[].todos[].todos[].todos").optional().description("목표 제목")
        );
    }

    public static ResponseFieldsSnippet getSuccessResponseFieldsSnippet() {
        return relaxedResponseFields(
                fieldWithPath("status").description("api 응답 상태"),
                fieldWithPath("message").description("api 응답 메시지"),
                fieldWithPath("data").description("api 응답 데이터")
        );
    }
}
