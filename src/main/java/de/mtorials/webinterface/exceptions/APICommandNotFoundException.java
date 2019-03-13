package de.mtorials.webinterface.exceptions;

public class APICommandNotFoundException extends RuntimeException{

    APICommandNotFoundExceptionObject commandNotFoundExceptionObject = new APICommandNotFoundExceptionObject();

    @Override
    public String getMessage(){

        return commandNotFoundExceptionObject.getMsg();
    }
}
