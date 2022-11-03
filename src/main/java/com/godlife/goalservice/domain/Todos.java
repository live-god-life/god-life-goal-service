package com.godlife.goalservice.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.godlife.goalservice.exception.NoSuchTodosInTodoException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Todos {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_todo_id")
	private final List<Todo> childTodos = new ArrayList<>();

	public Todos(List<Todo> childTodos) {
		this.childTodos.addAll(childTodos);
	}

	public LocalDate getFistStartDate() {
		return childTodos.stream()
			.min(Comparator.comparing(Todo::getStartDate))
			.orElseThrow(NoSuchTodosInTodoException::new)
			.getStartDate();
	}

	public LocalDate getLastEndDate() {
		return childTodos.stream()
			.max(Comparator.comparing(Todo::getEndDate))
			.orElseThrow(NoSuchTodosInTodoException::new)
			.getEndDate();
	}

	public List<Todo> get() {
		return Collections.unmodifiableList(childTodos);
	}

	public int getTotalTodoTaskCount() {
		int count = 0;
		for (Todo todo : childTodos) {
			if (todo instanceof TodoTask) {
				count++;
			} else {
				count += ((TodoFolder)todo).getChildTodos().get().size();
			}
		}
		return count;
	}

	public int getTotalTodoTaskScheduleCount() {
		int count = 0;
		for (Todo todo : childTodos) {
			if (todo instanceof TodoTask) {
				count += ((TodoTask)todo).getTotalTodoTaskScheduleCount();
			} else {
				for (Todo childTodo : ((TodoFolder)todo).getChildTodos().get()) {
					count += ((TodoTask)childTodo).getTotalTodoTaskScheduleCount();
				}
			}
		}
		return count;
	}
}
