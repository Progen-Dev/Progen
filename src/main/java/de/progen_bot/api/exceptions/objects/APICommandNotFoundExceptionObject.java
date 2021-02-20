package de.progen_bot.api.exceptions.objects;

public class APICommandNotFoundExceptionObject
{
    private static final String MESSAGE = "Command not found!";
    private static final int ERROR_CODE = 404;

    public String getMessage()
    {
        return MESSAGE;
    }

    public int getErrorCode()
    {
        return ERROR_CODE;
    }
}
