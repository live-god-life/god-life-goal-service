package com.godlife.goalservice.exception;

import java.util.NoSuchElementException;

public class NoSuchTodosInTodoEntityException extends NoSuchElementException {
	public NoSuchTodosInTodoEntityException() {
		super("Not found Todos in Todo");
	}
}
