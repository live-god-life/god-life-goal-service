package com.godlife.goalservice.domain;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class TodoTask extends Todo {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
