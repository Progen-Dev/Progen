package de.pwi.api.httpapi;

public class APIResponseObject {

    private final int rCode;
    private final Object object;

    public APIResponseObject(int rCode, Object object) {
        this.rCode = rCode;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public int getrCode() {
        return rCode;
    }
}
