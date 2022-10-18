package com.godlife.goalservice.util;

import com.godlife.goalservice.api.request.CreateGoalTodoRequest;

import java.util.List;

public class SampleDataCreator {
    public static CreateGoalTodoRequest createGoalTodoFolderRequest(String title) {
        return new CreateGoalTodoRequest(title, "folder");
    }

    public static CreateGoalTodoRequest getCreateGoalTodoTaskRequest(String title) {
        return new CreateGoalTodoRequest(title, "task");
    }

    public static CreateGoalTodoRequest getCreateGoalTodoFolderRequest(String title, List<CreateGoalTodoRequest> childTodoRequest) {
        return new CreateGoalTodoRequest(title, "folder", childTodoRequest);
    }
}
