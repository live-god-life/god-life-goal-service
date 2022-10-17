package com.godlife.goalservice.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("folder")
public class TodoFolder extends Todo {
    private String name;
}
