package com.godlife.goalservice.utils;

import com.godlife.goalservice.api.request.CreateGoalTodoRequest;

import java.util.List;

public class SampleTestDataCreator {
    public static CreateGoalTodoRequest getCreateGoalTodoTaskRequest(String title, Integer depth, Integer orderNumber, String startDate, String endDate, String repetitionType, List<String> repetitionParams, String notification) {
        return CreateGoalTodoRequest.builder()
                .type("task")
                .title(title)
                .depth(depth)
                .orderNumber(orderNumber)
                .startDate(startDate)
                .endDate(endDate)
                .repetitionType(repetitionType)
                .repetitionParams(repetitionParams)
                .notification(notification)
                .build();
    }

    public static CreateGoalTodoRequest getCreateGoalTodoFolderRequest(String title, Integer depth, Integer orderNumber, List<CreateGoalTodoRequest> childTodoRequest) {
        return CreateGoalTodoRequest.builder()
                .type("folder")
                .depth(depth)
                .orderNumber(orderNumber)
                .title(title)
                .todos(childTodoRequest)
                .build();
    }
}
