package de.progen_bot.api.entities;

public class APIResponseObject
{
    private final int rCode;
    private final Object object;

    public APIResponseObject(int rCode, Object object)
    {
        this.rCode = rCode;
        this.object = object;
    }

    public int getrCode()
    {
        return rCode;
    }

    public Object getObject()
    {
        return object;
    }
}
