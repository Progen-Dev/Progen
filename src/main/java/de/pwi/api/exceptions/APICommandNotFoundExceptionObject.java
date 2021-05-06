package de.pwi.api.exceptions;

public class APICommandNotFoundExceptionObject {

	private static final String MESSAGE = "Command not found!";
	private static final int ERROR_CODE = 404;

	public String getMsg() {
		return MESSAGE;
    }

    public int getrCode() {
		return ERROR_CODE;
    }
}
