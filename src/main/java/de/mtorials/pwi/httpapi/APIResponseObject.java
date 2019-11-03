package de.mtorials.pwi.httpapi;

public class APIResponseObject {

    private int rCode;
    private Object object;

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
