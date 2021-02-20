package de.progen_bot.api.exceptions;

import de.progen_bot.api.exceptions.objects.APICommandNotFoundExceptionObject;

public class APICommandNotFoundException extends APIException
{
    private static final long serialVersionUID = 1L;
    // TODO: 20.02.2021 static reference?
    private final APICommandNotFoundExceptionObject object = new APICommandNotFoundExceptionObject();

    @Override
    public String getMessage()
    {
        return this.object.getMessage();
    }
}
