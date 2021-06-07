package de.pwi.api.exceptions;

public class APICommandNotFoundException extends APIException {
    private static final long serialVersionUID = 1L;
    private final APICommandNotFoundExceptionObject commandNotFoundExceptionObject = new APICommandNotFoundExceptionObject();

    @Override
    public String getMessage() {

        return commandNotFoundExceptionObject.getMsg();
    }
}
