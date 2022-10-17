package com.godlife.goalservice.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("task")
public class TodoTask extends Todo {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
