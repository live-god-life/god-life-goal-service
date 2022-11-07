package com.godlife.goalservice.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@DiscriminatorValue("folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TodoFolder extends Todo {

	@Embedded
	private Todos childTodos;

	private TodoFolder(String title, Integer depth, Integer orderNumber, LocalDate startDate, LocalDate endDate, Todos childTodos, Goal goal) {
		super(title, depth, orderNumber, startDate, endDate, goal);
		this.childTodos = childTodos;
	}

	public static TodoFolder createTodoFolder(String title, Integer depth, Integer orderNumber, List<Todo> childTodos, Goal goal) {
		Todos todos = new Todos(childTodos);
		return new TodoFolder(title, depth, orderNumber, todos.getFistStartDate(), todos.getLastEndDate(), todos, goal);
	}
}

