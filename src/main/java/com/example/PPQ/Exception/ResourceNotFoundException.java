package com.example.PPQ.Exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
    // truy cap tai nguyen khong ton tai  (tim theo id nhung id khong ton tai )

}
