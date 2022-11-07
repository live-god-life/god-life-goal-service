package com.godlife.goalservice.exception;

import java.util.NoSuchElementException;

public class NoSuchTodoScheduleException extends NoSuchElementException {
	public NoSuchTodoScheduleException() {
		super("Not found Todo Schedule");
	}
}
