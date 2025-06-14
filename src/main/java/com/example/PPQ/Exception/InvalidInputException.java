package com.example.PPQ.Exception;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message){
        super(message);
    }
    // du lieu dau vao khong hop le ( du lieu sai dinh dang , bi thieu hoac sai logic)
    // kiem tra o pathvariable con MethodArgumentNotValidException  kiem tra o request body
}
