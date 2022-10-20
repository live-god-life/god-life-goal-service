package com.godlife.goalservice.utils;

import com.godlife.goalservice.api.request.CreateGoalTodoRequest;

import java.util.List;

public class SampleTestDataCreator {
    public static CreateGoalTodoRequest getCreateGoalTodoTaskRequest(String title, String startDate, String endDate, String repetitionType, List<String> repetitionParams, String notification) {
        return CreateGoalTodoRequest.builder()
                .type("task")
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .repetitionType(repetitionType)
                .repetitionParams(repetitionParams)
                .notification(notification)
                .build();
    }

    public static CreateGoalTodoRequest getCreateGoalTodoFolderRequest(String title, List<CreateGoalTodoRequest> childTodoRequest) {
        return CreateGoalTodoRequest.builder()
                .type("folder")
                .title(title)
                .todos(childTodoRequest)
                .build();
    }
}
