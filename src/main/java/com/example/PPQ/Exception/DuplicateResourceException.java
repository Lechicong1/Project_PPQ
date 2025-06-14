package com.example.PPQ.Exception;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message){
        super(message);
    }
    // du lieu bi trung lap ( tao user, email ,sdt bi trung lap )
}
