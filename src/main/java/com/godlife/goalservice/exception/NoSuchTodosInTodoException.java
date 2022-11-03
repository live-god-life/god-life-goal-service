package com.godlife.goalservice.exception;

import java.util.NoSuchElementException;

public class NoSuchTodosInTodoException extends NoSuchElementException {
	public NoSuchTodosInTodoException() {
		super("Not found Todos in Todo");
	}
}
