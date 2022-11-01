package com.godlife.goalservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Mindset extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mindsetId;

    @Lob
    private String content;

    private Mindset(String content) {
        this.content = content;
    }

    public static Mindset createMindset(String content) {
        return new Mindset(content);
    }
}
