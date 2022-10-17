package com.godlife.goalservice.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateGoalTodoRequest {
    private String title;
    private String type;
    private Integer depth;
    private Integer order;
    private List<CreateGoalTodoRequest> todos;

    public CreateGoalTodoRequest(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public CreateGoalTodoRequest(String title, String type, List<CreateGoalTodoRequest> todos) {
        this.title = title;
        this.type = type;
        this.todos = todos;
    }
}
