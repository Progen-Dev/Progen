package de.pwi.api.exceptions;

public class APIWrongParametersException extends APIException {
    private static final long serialVersionUID = 1L;
    public final String error = "wrong_parameter_usage";
}
