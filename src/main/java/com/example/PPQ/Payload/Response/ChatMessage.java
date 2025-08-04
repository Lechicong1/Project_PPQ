package com.example.PPQ.Payload.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String sender;   // người gửi (username)
    private String receiver; // người nhận (username)
    private String content;


}
