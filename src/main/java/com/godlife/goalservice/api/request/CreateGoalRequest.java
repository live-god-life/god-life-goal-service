package com.godlife.goalservice.api.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateGoalRequest {
    private String title;

    //TODO category 처리 어떻게할까
    private String categoryName;
    private String categoryCode;

    private List<CreateGoalMindsetRequest> mindsets;
    private List<CreateGoalTodoRequest> todos;
}
