package de.mtorials.webinterface.exceptions;

public class APICommandNotFoundExceptionObject {

    private String msg = "Command not found!";
    private int rCode = 404;

    public String getMsg() {
        return msg;
    }

    public int getrCode() {
        return rCode;
    }
}
