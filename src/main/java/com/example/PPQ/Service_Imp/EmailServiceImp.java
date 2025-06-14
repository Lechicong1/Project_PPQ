package com.example.PPQ.Service_Imp;

import com.example.PPQ.Payload.Request.contactRequest;

public interface EmailServiceImp {
    boolean sendContactEmail(contactRequest contact);
}
