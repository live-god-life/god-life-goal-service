package com.godlife.goalservice.domain;

import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
public class TodoTaskSchedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("일정")
    private LocalDate scheduleDate;

    @Comment("완료여부")
    private Boolean completionStatus;

    public TodoTaskSchedule(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
        this.completionStatus = false;
    }
}
