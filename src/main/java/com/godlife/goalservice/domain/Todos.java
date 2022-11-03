package com.godlife.goalservice.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.godlife.goalservice.exception.NoSuchTodosInTodoEntityException;

@Embeddable
public class Todos {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_todo_id")
	private final List<Todo> childTodos;

	public Todos(List<Todo> childTodos) {
		this.childTodos = childTodos;
	}

	public LocalDate getFistStartDate() {
		return childTodos.stream()
			.min(Comparator.comparing(Todo::getStartDate))
			.orElseThrow(NoSuchTodosInTodoEntityException::new)
			.getStartDate();
	}

	public LocalDate getLastEndDate() {
		return childTodos.stream()
			.max(Comparator.comparing(Todo::getEndDate))
			.orElseThrow(NoSuchTodosInTodoEntityException::new)
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
